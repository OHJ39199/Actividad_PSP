package transferenciaArchivosSockets;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

public class ClienteArchivos {
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 5002;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVIDOR, PUERTO);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            // Pedir nombre del archivo a enviar
            System.out.print("Introduzca la ruta del archivo a enviar: ");
            String rutaArchivo = teclado.readLine();
            File archivo = new File(rutaArchivo);

            if (!archivo.exists()) {
                System.out.println("El archivo no existe.");
                return;
            }

            // Enviar nombre del archivo
            salida.writeUTF(archivo.getName());

            // Enviar tamaño del archivo
            salida.writeLong(archivo.length());

            // Enviar contenido del archivo
            try (FileInputStream fis = new FileInputStream(archivo)) {
                byte[] buffer = new byte[4096];
                int bytesLeidos;

                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    salida.write(buffer, 0, bytesLeidos);
                }
            }

            // Recibir confirmación del servidor
            String respuesta = entrada.readUTF();
            System.out.println("Servidor: " + respuesta);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
