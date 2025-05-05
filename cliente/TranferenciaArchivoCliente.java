package cliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TranferenciaArchivoCliente {
	private static final String HOST = "10.140.132.36";
	private static final int PUERTO = 5002;
	
	
	public static void main(String[] args) {
		
		try (Socket socket = new Socket (HOST, PUERTO)){
			//coonfirmacion conexion servidor
			System.out.println("conectado al servidor: "+ HOST);
			
			//input output streams
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			BufferedReader tc = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
				//ruta archivo local
				System.out.println("introduce la ruta del archivo (o 'FIN' para salir): ");
				String filePath = tc.readLine();

				// Verificar si el usuario quiere terminar
		                if (filePath.equalsIgnoreCase("FIN")) {
		                    dos.writeUTF("FIN");
		                    System.out.println("Sesión terminada.");
		                    break;
		                }
				
				// comprobar existe archivo
				File file = new File(filePath);
				if (!file.exists() || !file.isFile()) {
					System.out.println("Error: el documento no existe o no es un formato valido");
					socket.close();
					return;
				}
				
				//enviar archivo
				dos.writeUTF(file.getName());
				dos.writeLong(file.length());
				
				try (FileInputStream fis = new FileInputStream(file)){
					byte[] buffer = new byte[4096];
					int bytesLeidos;
					
					while((bytesLeidos = fis.read(buffer)) != -1) {
						dos.write(buffer, 0, bytesLeidos);
					}
				}
			
				//confirmacion servidot
				String respuesta = dis.readUTF();
				System.out.println("servidor: " + respuesta);
	
				// Preguntar si desea enviar otro archivo
		                System.out.println("¿Desea enviar otro archivo? (S/N): ");
		                String respuestaUsuario = tc.readLine();
		                if (!respuestaUsuario.equalsIgnoreCase("S")) {
		                    dos.writeUTF("FIN");
		                    System.out.println("Sesión terminada.");
		                    break;
		                }
			}
			
		} catch (UnknownHostException e) {
			System.err.println("Error: No se pudo encontrar el host " + HOST);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al comunicarse con el servidor.");
			e.printStackTrace();
		}
	}
}
