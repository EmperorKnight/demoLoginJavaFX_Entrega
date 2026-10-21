package ni.edu.uam.demologin.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ArchivoService {

    private static final String DIRECTORIO_UPLOADS="./uploads";

    public String guardarArchivo(File archivo) throws IOException {
        crearDirectorioUploads();
        String extension = obtenerExtension(archivo);
        String nombreArchivo = UUID.randomUUID() + extension;

        Path destino = Paths.get(DIRECTORIO_UPLOADS)
                .toAbsolutePath()
                .normalize()
                .resolve(nombreArchivo);
        Files.copy(archivo.toPath()
                , destino
                , StandardCopyOption.REPLACE_EXISTING);
        return destino.toString();
    }

    private String obtenerExtension(File archivo) {
        String nombre = archivo.getName();
        int pos = nombre.lastIndexOf('.');
        if (pos > 0) {
            return nombre.substring(pos);
        }
        return "";
    }

    private void crearDirectorioUploads() throws IOException {
        Files.createDirectories(Paths.get(DIRECTORIO_UPLOADS));
    }
}
