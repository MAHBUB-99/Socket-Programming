# 🌐 Java Networking & Socket Programming — Important Concepts with Basic Code

---

## 1️⃣ What is Socket Programming?
- Enables communication between **two programs** over a network
- Uses **IP Address + Port**
- Java supports:
  - **TCP (Reliable)** → `Socket`, `ServerSocket`
  - **UDP (Fast, Unreliable)** → `DatagramSocket`

---

## 2️⃣ TCP vs UDP (Core Difference)

| Feature | TCP | UDP |
|------|----|----|
| Connection | Yes | No |
| Reliable | Yes | No |
| Order | Maintained | Not guaranteed |
| Speed | Slower | Faster |
| Java Class | Socket | DatagramSocket |

---

## 3️⃣ Client–Server Architecture
- **Server**: waits for requests
- **Client**: sends request
- Server can handle **multiple clients** using threads

---

## 4️⃣ ServerSocket (TCP Server)
- Listens on a port
- Accepts client connections

### Basic TCP Server
```java
ServerSocket serverSocket = new ServerSocket(5000);
Socket clientSocket = serverSocket.accept();

System.out.println("Client connected");
```
---
## 5️⃣ Socket (TCP Client)

- Connects to server using **IP Address + Port**
- Used on the **client side** for TCP communication

### Basic TCP Client
```java
Socket socket = new Socket("localhost", 5000);
System.out.println("Connected to server");
```
---
## 6️⃣ Data Communication (Streams)

### Concept
- Java sockets exchange data using **streams**
- Communication happens in the form of **bytes or characters**
- Streams are obtained from the `Socket` object
- Using **buffered streams** improves performance and efficiency

### Types of Streams
- **InputStream** → receives data from the other side
- **OutputStream** → sends data to the other side
- Character-based wrappers:
  - `InputStreamReader`
  - `BufferedReader`
  - `PrintWriter`

### Sending Data (Server or Client)
- `PrintWriter` is used for sending text data
- `true` enables **auto-flush**

```java
PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
out.println("Hello Server");
```

## Receiving Data (Server or Client)

- `BufferedReader` reads text **line by line**
- It **blocks** the current thread until data is available
- Commonly used for receiving text messages over TCP

```java
BufferedReader in = new BufferedReader(
    new InputStreamReader(socket.getInputStream())
);
String msg = in.readLine();
```

## 7️⃣ Full Simple TCP Example

This example demonstrates a **basic TCP client–server communication**  
where the client sends a message and the server receives it.

---

### 🖥️ TCP Server

**Responsibilities**
- Creates a server socket on a specific port
- Waits for a client connection
- Reads the message sent by the client

```java
ServerSocket server = new ServerSocket(5000);
System.out.println("Server started, waiting for client...");

Socket socket = server.accept(); // waits for client
System.out.println("Client connected");

BufferedReader in = new BufferedReader(
    new InputStreamReader(socket.getInputStream())
);

String message = in.readLine();
System.out.println("Received from client: " + message);

socket.close();
server.close();
```
---
Key Points to Remember

- accept() and readLine() are blocking operations

- Server must be started before the client

- Always close sockets to free system resources

- TCP ensures reliable and ordered data delivery
---
