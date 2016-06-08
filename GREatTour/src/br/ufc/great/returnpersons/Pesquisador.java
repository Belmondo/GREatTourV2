package br.ufc.great.returnpersons;

import java.util.ArrayList;

/**
 * @author Belmondo Rodrigues
 * @author Edmilson Rocha
 *
 */

public class Pesquisador {

	private String Nome,about_en,lattes,fotoUrl,Expertise_Area;





	public Pesquisador() {
	}

	public Pesquisador(String Nome, String about_en,String lattes,String fotoUrl,String Expertise_Area) {

		this.Nome = Nome;

		this.fotoUrl = fotoUrl;
		this.about_en = about_en;
		this.lattes = lattes;
		this.Expertise_Area = Expertise_Area;




	}



	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getFotoUrl() {
		return fotoUrl;
	}

	public void setFotoUrl(String fotoUrl) {
		this.fotoUrl = fotoUrl;
	}

	public String getAbout_en() {
		return about_en;
	}

	public void setAbout_en(String about_en) {
		this.about_en = about_en;
	}

	public String getLattes() {
		return lattes;
	}

	public void setLattes(String lattes) {
		this.lattes = lattes;
	}

	public String getExpertise_Area() {
		return Expertise_Area;
	}

	public void setExpertise_Area(String expertise_Area) {
		this.Expertise_Area = expertise_Area;
	}





}
