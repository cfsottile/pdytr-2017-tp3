## 0

### ¿Cómo funciona RMI?

Se definen clases cuyas instancias podrán ser *publicadas* por el servidor y
estarán disponibles para utilizar por los clientes.

Para que las clases puedan publicarse pueden extender `UnicastRemoteObject` e
implementar `Remote`.

En el servidor, se invoca a `Naming.rebind(URI, remoteObject)` para publicar al
objeto. Luego, en el cliente, se invoca a `Naming.lookup(URI)` para obtener la
instancia del objeto remoto. Así queda listo el objeto remoto para utilizarse
de forma transparente como si fuera local.

## 1

### a

Es transparente desde el punto de vista de que la sintaxis de invocación de las
funciones remotas es igual que la de las funciones locales. El programador de
la clase remota sólo debe hacerla implementar la interfaz `Remote`, y cada
método debe lanzar `RemoteException`.

### b

Del lado del cliente deberían estar `AskRemote.class`, `IfaceRemoteClass.class`
y `RemoteClass.class`; o bien, `AskRemote.class`, `IfaceRemoteClass.class` y un
`RemoteClass_Stub.class` generado con `rmic`.

Del lado del servidor deberían estar `StartRemoteObject.class`,
`IfaceRemoteClass.class` y `RemoteClass.class`.

## 2

En términos conceptuales, RPC y RMI apuntan a lo mismo: invocar, en una
máquina, procedicimientos que serán ejecutados en otra máquina. Ambos aspiran a
tener un gran nivel de transparencia, de forma tal que la invocación a los
procedimientos remotos sea similar a la invocación a procedimientos locales.
Por ser una tecnología de Java, RMI presenta un fuerte arraigo al paradigma de
objetos, mientras que RPC, por estar ligado a C, se basa en la programación
estructurada.

## 3

El problema es que alguno de los procesos involucrados no tenga en su CLASSPATH
el archivo correspondiente a alguna de las clases involucradas en las
comunicaciones. Se soluciona mediante la carga dinámica de clases, que consiste
configurar un RMISecurityManager y definir la URL desde donde se obtendrá la
clase en cuestión. Esto permite, por ejemplo, ampliar la funcionalidad de un
servidor a partir de modificaciones en el cliente, y viceversa.

## 4

Las clases involucradas son:

* `Server`: determina la operatoria del servidor de archivos.
* `StartServer`: registra a los objetos remotos (en este caso será un solo objeto, el servidor), dejando todo listo para que sea invocado por los clientes.
* `Client`: determina la operatoria de los clientes.

Y además tenemos a la interfaz `IRemoteServer`.

## 5

Para la demostración de la concurrencia de un llamado a un método remoto, comunicamos dos clientes para que invoquen al mismo método.  El metodo remoto se queda loopeando e imprimiendo por pantalla quién fue el cliente que lo llamo. La salida del servidor fue la siguiente:

```
$ java Server
Me llego algo de: cliente 1 a la 1
Me llego algo de: cliente 2 a la 1
Me llego algo de: cliente 1 a la 2
Me llego algo de: cliente 2 a la 2
Me llego algo de: cliente 1 a la 3
Me llego algo de: cliente 2 a la 3
Me llego algo de: cliente 1 a la 4
Me llego algo de: cliente 2 a la 4
Me llego algo de: cliente 1 a la 5
Me llego algo de: cliente 2 a la 5
Termine con cliente 1
Termine con cliente 2
```

RMI genera un nuevo thread para cada cliente, es así que por defecto la concurrencia es real, a diferencia de RPC en donde los llamados de clientes se atienden en forma secuencial, por lo tanto no hay concurrencia.

No es apropiado para el caso del servidor, ya que dos clientes podrían invocar el método `write` con un mismo nombre de archivo, y se estaría generando un archivo que contendría a los dos archivos mezclados. Solución propuesta: separar en casos el método `write`:

* en caso de que no exista el archivo, se crea, se agrega un identificador del cliente actual en un diccionario de archivos y clientes activos, y se procede a escribir los bytes recibidos;
* en caso de que exista el archivo, se revisa si está activo:
	* si lo está, se chequea que el cliente sea el mismo del diccionario y se procede a escribir;
	* si no lo está, se agrega archivo-cliente al diccionario y se procede a escribir.

## 6

### a

 Para calcular el tiempo de respuesta de una invocacion a un metodo remoto, en
el script del cliente tomamos la fecha/hora en la sentencia anterior al
llamado al metodo asi como tambien en la sentencia siguiente. Al hacer
la diferencia de estas dos fechas tenemos el tiempo en milisegundos del
llamado.

  Al realizar 10 llamados al servidor los tiempos fueron los siguientes:

    10 ms
    4 ms
    3 ms
    3 ms
    4 ms
    5 ms
    6 ms
    5 ms
    6 ms
    6 ms

*  Pruebas: 10
*  Total: 52
*  Promedio: 5,2 ms
*  Desviacion Estandar: 2.04396

### b

Una de las opciones que se tiene para poder cambiar el timeout predefinido que tiene RMI es redefinir la configuracion del Socket que utiliza para comunicarse.
Ej:

```java
RMISocketFactory.setSocketFactory(new RMISocketFactory() {
  public Socket createSocket(String host, int port) throws IOException {
    Socket socket = new Socket(host, port);
    socket.setSoTimeout(timeoutMillis);
    socket.setSoLinger(false, 0);
    return socket;
  }
  public ServerSocket createServerSocket(int port) throws IOException {
    return new ServerSocket(port);
  }
});
```

Otra posible es cambiar el timeout de las respuestas a travez de las
propiedades del sistema.

Ej:

```java
System.setProperty("sun.rmi.transport.tcp.responseTimeout", "10000");
```

Para la demostracion de esto configuramos con el ejemplo provisto
anteriormente un cliente con un timeout de 2 segundos, y al servidor remoto un
retrazo de 10 segundos. Al vencer el tiempo definido por el cliente se levanta
una exception.