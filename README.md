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
