/**
 * 
 */
package uk.co.jemos.protomak.engine.api;

/**
 * Main contract for conversion services.
 * 
 * @author mtedone
 * 
 */
public interface ConversionService {

	/**
	 * Given an input file, it generates one or more proto files and places them
	 * in the given output folder.
	 * 
	 * @param inputPath
	 *            The full path to an input file to use for the generation of
	 *            proto files.
	 * @param outputPath
	 *            The folder where to place the generated files.
	 */
	public void generateProtoFiles(String inputPath, String outputPath);

	/**
	 * It generates a proto file for each file of the given extension in the
	 * input folder.
	 * 
	 * @param inputFolder
	 *            This must be the full path to an input folder and it must
	 *            exist.
	 * @param outputFolder
	 *            The folder where the proto files will be generated. If it does
	 *            not exist, one will be created for you.
	 * @param fileExtension
	 *            The extension of the files to filter in the input folder.
	 * 
	 * @throws IllegalArgumentException
	 *             If the input folder does not exist.
	 */
	public void generateProtoFiles(String inputFolder, String outputFolder,
			String fileExtension);

}
