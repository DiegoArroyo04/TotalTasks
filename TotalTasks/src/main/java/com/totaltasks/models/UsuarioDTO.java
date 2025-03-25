package com.totaltasks.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioDTO {

    private String nombre;
    private String usuario;
    private String email;
    private String contrasenia;
    
    private byte[] fotoPerfil;
    private String fotoURL;  // Campo para manejar la URL directamente

    // Método para convertir de byte[] a String (URL)
    public String getFotoURL() {
        return (this.fotoPerfil != null) ? new String(this.fotoPerfil) : null;
    }

    // Método para convertir de String (URL) a byte[]
    public void setFotoURL(String url) {
        this.fotoPerfil = (url != null) ? url.getBytes() : null;
    }

    // CONSTRUCTOR PARA LOGIN
    public UsuarioDTO(String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public UsuarioDTO(String nombre, String usuario, String email, String contrasenia, String fotoURL) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.email = email;
        this.contrasenia = contrasenia;
        setFotoURL(fotoURL); // Guarda la URL como byte[]
    }
}