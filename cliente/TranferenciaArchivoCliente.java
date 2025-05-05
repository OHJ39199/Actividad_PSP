package cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
			//ruta archivo local
			System.out.println("introduce la ruta del archivo: ");
			String filePath = tc.readLine();
			
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
			System.out.println("servidor: "+ respuesta);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
