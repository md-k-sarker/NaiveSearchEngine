/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wright.dase.model;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.lucene.store.Directory;

import edu.wright.dase.indexing.BuildIndex;

/**
 *
 * @author mdkamruzzamansarker
 */
public class CONSTANTS {
    
	public static String OSNAME = "";
	
    public static Charset ENCODING = StandardCharsets.UTF_8;
    
    public static double THRESHOLD = .1;
    
    public static String INDEXPATH = "resources/indexes";
    
    public static String INPUTFILESDIRECTORY = ""; 
    
    public static String RESULTSPATH = "./";
    
    public static long indexsearchduration = 0;
    
    public static long displayduration = 0;
   
    public static long totalduration = 0;
    
    public static long FetchEachRelevantDoc = 0;
    
    
    
    public static String[] CranfieldFirst25Queries= {"what similarity laws must be obeyed when constructing aeroelastic models\n" +
"of heated high speed aircraft .","what are the structural and aeroelastic problems associated with flight\n" +
"of high speed aircraft .","what problems of heat conduction in composite slabs have been solved so\n" +
"far .","can a criterion be developed to show empirically the validity of flow\n" +
"solutions for chemically reacting gas mixtures based on the simplifying\n" +
"assumption of instantaneous local chemical equilibrium .","what chemical kinetic system is applicable to hypersonic aerodynamic\n" +
"problems .","what theoretical and experimental guides do we have as to turbulent\n" +
"couette flow behaviour .","is it possible to relate the available pressure distributions for an\n" +
"ogive forebody at zero angle of attack to the lower surface pressures of\n" +
"an equivalent ogive forebody at angle of attack ","what methods -dash exact or approximate -dash are presently available\n" +
"for predicting body pressures at angle of attack.","papers on internal /slip flow/ heat transfer studies .","are real-gas transport properties for air available over a wide range of\n" +
"enthalpies and densities .","is it possible to find an analytical,  similar solution of the strong\n" +
"blast wave problem in the newtonian approximation ","how can the aerodynamic performance of channel flow ground effect\n" +
"machines be calculated","what is the basic mechanism of the transonic aileron buzz ","papers on shock-sound wave interaction","material properties of photoelastic materials","can the transverse potential flow about a body of revolution be\n" +
"calculated efficiently by an electronic computer","can the three-dimensional problem of a transverse potential flow about\n" +
"a body of revolution be reduced to a two-dimensional problem","are experimental pressure distributions on bodies of revolution at angle\n" +
"of attack available ","does there exist a good basic treatment of the dynamics of re-entry\n" +
"combining consideration of realistic effects with relative simplicity of\n" +
"results","has anyone formally determined the influence of joule heating,  produced\n" +
"by the induced current,  in magnetohydrodynamic free convection flows\n" +
"under general conditions","why does the compressibility transformation fail to correlate the high\n" +
"speed data for helium and air ","did anyone else discover that the turbulent skin friction is not over\n" +
"sensitive to the nature of the variation of the viscosity with\n" +
"temperature ","what progress has been made in research on unsteady aerodynamics","what are the factors which influence the time required to invert large\n" +
"structural matrices ","does a practical flow follow the theoretical concepts for the\n" +
"interaction between adjacent blade rows of a supersonic cascade "};
    //Paths.get (resource.toURI()).toFile();
}
