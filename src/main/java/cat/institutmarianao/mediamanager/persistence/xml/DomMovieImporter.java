package cat.institutmarianao.mediamanager.persistence.xml;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cat.institutmarianao.mediamanager.model.Movie;
import cat.institutmarianao.mediamanager.model.Series;
import cat.institutmarianao.mediamanager.persistence.ExerciseNotImplementedException;
import org.w3c.dom.Element;

/**
 * Student exercise for importing movies_dom.xml using DOM.
 * File access must use traditional Java I/O. This class does not manage a Catalog.
 */
public class DomMovieImporter {
    /**
     * Creates a stateless importer without opening any files.
     */
    public DomMovieImporter() {
    }

    /**
     * Imports the source using DOM, as required by Task 1.
     *
     * <p>Return a non-null list of newly constructed domain objects in document
     * order, with internal codes left at zero. Preserve all required descriptive
     * information and any complete series, season and episode hierarchy.
     * A valid empty source produces an empty list. Malformed or invalid data
     * must be reported, not silently discarded.
     *
     * <p>Use traditional Java I/O, respect the source encoding (UTF-8 for the
     * supplied files), close opened resources and do not print to the console.
     * Do not depend on Catalog or assign internal codes. Binding classes, where
     * required, are part of the student exercise and must remain outside the model.
     *
     * @param input source file; never null
     * @return newly constructed, uncoded contents in source order; never null
     * @throws NullPointerException if input is null after implementation
     * @throws IOException if reading, parsing, binding, configuration or data
     *         validation fails; preserve the cause and useful file context
     * @throws ExerciseNotImplementedException while this exercise is unfinished
     */
    public List<Movie> importMovies(File input) throws IOException {
    	ArrayList<Movie> lista = new ArrayList();
    	final String ETIQUETA_NAME = "name";
    	final String ETIQUETA_GENRE = "genre";
    	final String ETIQUETA_RELEASEDATE = "releaseDate";
    	final String ETIQUETA_DIRECTOR = "director";
    	final String ETIQUETA_PLATFORM = "platform";
    	final String ETIQUETA_RUNTIME = "runtime";
    	try {
    		
			// 1) Configuració i creació del parser
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			// Protecció contra XML externs (XXE)
			f.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			// Si l'XML té namespaces
			f.setNamespaceAware(true);
			// Ignorar espais en blanc i comentaris
			f.setIgnoringElementContentWhitespace(true);
			f.setIgnoringComments(true);
			// Crear el parser
			DocumentBuilder b = f.newDocumentBuilder();

			// 2) Parsejar el document
			Document doc = b.parse(new File("data/input/movies_dom.xml"));

			// 3) Normalitzar (opcional)
			doc.getDocumentElement().normalize();
    		
			NodeList llista1 = doc.getFirstChild().getChildNodes();
			for (int i = 0; i < llista1.getLength(); i++) {
				// Comprovar que és un Element
				if (llista1.item(i) instanceof Element) {
					Node e = llista1.item(i);

					// Obtenir fills
					NodeList fills = e.getChildNodes();
					String titol = "";
					String genero = "";
					String director = "";
					LocalDate fechaLanzamiento = LocalDate.now();
					String plataforma = "";
					int duracion = 0;
					NodeList llista = doc.getFirstChild().getChildNodes();
					for (int j = 0; j < fills.getLength(); j++) {
						if (fills.item(j) instanceof Element) {
							Node fill = fills.item(j);
							if (fill.getNodeName().equals(ETIQUETA_NAME)) {
								titol = fill.getTextContent();
							}
							else if (fill.getNodeName().equals(ETIQUETA_GENRE)) {
								genero = fill.getTextContent();
							} 
							else if(fill.getNodeName().equals(ETIQUETA_RELEASEDATE)) {
								String textoFecha = fill.getTextContent();
								fechaLanzamiento = LocalDate.parse(textoFecha);
							} 
							else if(fill.getNodeName().equals(ETIQUETA_PLATFORM)) {
								plataforma = fill.getTextContent();
							}
							else if(fill.getNodeName().equals(ETIQUETA_DIRECTOR)) {
							    Node nodoNombre = fill.getChildNodes().item(1);
							    if (nodoNombre.getNodeName().equals(ETIQUETA_NAME)) {
							        director = nodoNombre.getTextContent();
							    }
							}
							else if(fill.getNodeName().equals(ETIQUETA_RUNTIME)) {
								String textNumero = fill.getTextContent();
								duracion = Integer.parseInt(textNumero);
							}
						}
					}					
					lista.add(new Movie(titol,director,genero,fechaLanzamiento,plataforma,duracion));
				}
			}
    		
    		
    	}catch(Exception e) {
			e.printStackTrace();
		}

    	return lista;
    }
}
