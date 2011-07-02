package uk.co.jemos.protomak;

import org.apache.commons.cli.*;

import uk.co.jemos.protomak.engine.api.*;
import uk.co.jemos.protomak.engine.impl.*;

/**
 * Class used to launch Protomak as a stand alone tool.
 * <p>
 * Valid arguments can be obtained with the -? argument.
 */
public final class ProtomakLauncher {
	
	public static void main( String[] args ) {
	    // create the parser
	    CommandLineParser parser = new GnuParser();
	    try {
	    	Options options = createOptions();
	        String file;
	        String input;
	        String out = ".";
	        
	    	// parse the command line arguments
	        CommandLine line = parser.parse(options, args );
	        
	        if (line.hasOption("help")) {
	        	printUsage(options);
	        	return;
	        }	        
	        
	        file = line.getOptionValue("file");
	        input = line.getOptionValue("input");
	        
	        if (file == null && input == null) {
	        	printUsage(options);
	        	return;
	        }
	        	
	        if (line.hasOption("out")) {
	        	out = line.getOptionValue("out");	        		        
	        }
	        
	        ConversionService service = new XsomXsdToProtoDomainConversionServiceImpl();	        
	        if (file != null)
	        	service.generateProtoFiles(file, out);
	        else
	        	service.generateProtoFiles(input, out, "xsd");
	    }
	    catch( ParseException exp ) {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	    }
	}

	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "protomak", options);
	}
	
	@SuppressWarnings("static-access")
	private static Options createOptions() {
		 Options options = new Options();
		 options.addOption(OptionBuilder
					.withArgName("file")
					.hasArg()
					.withDescription("xsd schema to convert")
					.create("file"));
		 
		 options.addOption(OptionBuilder
					.withArgName("input")
					.hasArg()
					.withDescription("folder containing xsd's to process")
					.create("input"));
	 
		 options.addOption(OptionBuilder
					.withArgName("out")
					.hasArg()
					.withDescription("output directory, default is current directory")
					.create("out"));
         
	 	 options.addOption(new Option( "help", "print this message" ));
	 	 
         return options;
	}

}
