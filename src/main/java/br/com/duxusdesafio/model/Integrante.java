package br.com.duxusdesafio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "integrante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Integrante {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column
	private String franquia;
	
	@NotNull
	@Column
	private String nome;
	
	@NotNull
	@Column
	private String funcao;
	
	@OneToMany(mappedBy = "integrante")
	private List<ComposicaoTime> composicaoTime;

	public Integrante(String franquia, String nome, String funcao, List<ComposicaoTime> composicaoTime) {
		this.franquia = franquia;
		this.nome = nome;
		this.funcao = funcao;
		this.composicaoTime = composicaoTime;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Integrante)) return false;
		Integrante that = (Integrante) o;
		return id == that.id && Objects.equals(franquia, that.franquia) && Objects.equals(nome, that.nome) && Objects.equals(funcao, that.funcao);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, franquia, nome, funcao);
	}

	@Override
	public String toString() {
		return "Integrante{" +
				"id=" + id +
				", franquia='" + franquia + '\'' +
				", nome='" + nome + '\'' +
				", funcao='" + funcao + '\'' +
				'}';
	}
}
