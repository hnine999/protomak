package uk.co.jemos.protomak;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import uk.co.jemos.protomak.engine.api.ConversionService;
import uk.co.jemos.protomak.engine.impl.XsomXsdToProtoDomainConversionServiceImpl;

/**
 * Goal which triggers the generation of proto files for all XSDs in the input
 * folder.
 * 
 * @goal all-xsds-to-proto
 * 
 * @phase process-sources
 * 
 * @requiresDependencyResolution compile
 */
public class AllXsdsToProtoMojo extends AbstractMojo {

	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${inputFolder}"
	 * @required
	 */
	private String inputFolder;

	/**
	 * The location where the generated files will be placed.
	 * 
	 * @parameter expression="${outputFolder}"
	 * @required
	 */
	private String outputFolder;

	/**
	 * The extension of the files to consider in the input folder.
	 * 
	 * @parameter expression="${extension}" default-value=".xsd"
	 * @required
	 * 
	 */
	private String extension;

	public void execute() throws MojoExecutionException {

		// Will use the default serialisation strategy
		ConversionService conversionService = new XsomXsdToProtoDomainConversionServiceImpl();
		this.getLog().info(
				"Invoking the XSD to Proto conversion for all " + extension
						+ " files in " + inputFolder);
		conversionService.generateProtoFiles(inputFolder, outputFolder,
				extension);
		this.getLog().info(
				"XSOM conversion from XSD to Proto completed successfully");

	}
}
