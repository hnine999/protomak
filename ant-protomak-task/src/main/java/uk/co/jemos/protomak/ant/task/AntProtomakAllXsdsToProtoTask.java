/**
 * 
 */
package uk.co.jemos.protomak.ant.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import uk.co.jemos.protomak.engine.api.ConversionService;
import uk.co.jemos.protomak.engine.impl.XsomXsdToProtoDomainConversionServiceImpl;

/**
 * Ant task to launch the XSD to Proto conversion.
 * 
 * @author mtedone
 * 
 */
public class AntProtomakAllXsdsToProtoTask extends Task {

	// ------------------->> Constants

	// ------------------->> Instance / Static variables

	/** The input XSD file */
	private String inputFolder;

	/** The output Folder */
	private String outputFolder;

	/** The extension of files to take into consideration */
	private String extension;

	// ------------------->> Constructors

	// ------------------->> Public methods

	@Override
	public void execute() throws BuildException {

		this.log("Invoking XSD to Proto conversion for all " + extension
				+ " files in " + inputFolder, Project.MSG_INFO);
		ConversionService conversionService = new XsomXsdToProtoDomainConversionServiceImpl();
		conversionService.generateProtoFiles(inputFolder, outputFolder,
				extension);
		this.log("XSD to Proto conversion completed...", Project.MSG_INFO);

	}

	// ------------------->> Getters / Setters

	/**
	 * @param inputXsdFile
	 *            the inputXsdFile to set
	 */
	public void setInputXsdFile(String inputXsdFile) {
		inputFolder = inputXsdFile;
	}

	/**
	 * @param outputFolder
	 *            the outputFolder to set
	 */
	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	/**
	 * @param extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	// ------------------->> Private methods

	// ------------------->> equals() / hashcode() / toString()

	// ------------------->> Inner classes

}
