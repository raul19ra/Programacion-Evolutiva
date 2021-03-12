package algoritmoGenetico.mutacion;

import java.util.List;

import algoritmoGenetico.individuos.Individuo;
import utils.SortedArrayList;

public abstract class Mutacion {
	protected int tipoSeleccion;
	protected List<Individuo> poblacion;
	protected int tamPoblacion;
	
	
	public Mutacion(List<Individuo> _poblacion, int _tamPoblacion) {
		this.poblacion= new SortedArrayList<>();
		for(int i=0;i<_tamPoblacion;i++)
			poblacion.add(_poblacion.get(i)); //Revisar esta copia
		this.tamPoblacion=_tamPoblacion;
	}
	
	public abstract List<Individuo> selecciona();
}

