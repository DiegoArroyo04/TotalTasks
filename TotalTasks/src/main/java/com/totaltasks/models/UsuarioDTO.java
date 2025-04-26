package com.totaltasks.models;

import java.util.Base64;

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
	private String fotoPerfilGoogle;
	private String fotoPerfilGithub;

	public String getFotoPerfilBase64() {
		if (this.fotoPerfil != null) {
			return Base64.getEncoder().encodeToString(this.fotoPerfil);
		}
		return null;
	}

	// CONSTRUCTOR PARA LOGIN MANUAL
	public UsuarioDTO(String usuario, String contrasenia) {
		this.usuario = usuario;
		this.contrasenia = contrasenia;
	}

}