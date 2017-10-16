## 0

### ¿Cómo funciona RMI?

Se definen clases cuyas instancias podrán ser *publicadas* por el servidor y estarán disponibles para utilizar por los clientes.

Para que las clases puedan publicarse pueden extender `UnicastRemoteObject` e implementar `Remote`.

En el servidor, se invoca a `Naming.rebind(URI, remoteObject)` para publicar al objeto. Luego, en el cliente, se invoca a `Naming.lookup(URI)` para obtener la instancia del objeto remoto. Así queda listo el objeto remoto para utilizarse de forma transparente como si fuera local.

## 1

### a

Es transparente desde el punto de vista de que la sintaxis de invocación de las funciones remotas es igual que la de las funciones locales. El programador de la clase remota sólo debe hacerla implementar la interfaz `Remote`, y cada método debe lanzar `RemoteException`.

### b

Del lado del cliente deberían estar `AskRemote.class`, `IfaceRemoteClass.class` y `RemoteClass.class`; o bien, `AskRemote.class`, `IfaceRemoteClass.class` y un `RemoteClass_Stub.class` generado con `rmic`.

Del lado del servidor deberían estar `StartRemoteObject.class`, `IfaceRemoteClass.class` y `RemoteClass.class`.

## 2

En términos conceptuales, RPC y RMI apuntan a lo mismo: invocar, en una máquina, procedicimientos que serán ejecutados en otra máquina. Ambos aspiran a tener un gran nivel de transparencia, de forma tal que la invocación a los procedimientos remotos sea similar a la invocación a procedimientos locales. Por ser una tecnología de Java, RMI presenta un fuerte arraigo al paradigma de objetos, mientras que RPC, por estar ligado a C, se basa en la programación estructurada.

## 3

El problema es que alguno de los procesos involucrados no tenga en su CLASSPATH el archivo correspondiente a alguna de las clases involucradas en las comunicaciones. Se soluciona mediante la carga dinámica de clases, que consiste configurar un RMISecurityManager y definir la URL desde donde se obtendrá la clase en cuestión. Esto permite, por ejemplo, ampliar la funcionalidad de un servidor a partir de modificaciones en el cliente, y viceversa.

## 4

Clases involucradas:

* `Server`
* `StartServer`
* `Client`