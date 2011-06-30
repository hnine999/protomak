/**
 * 
 */
package uk.co.jemos.protomak.engine.test.unit;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import uk.co.jemos.protomak.engine.exceptions.ProtomakXsdToProtoConversionError;
import uk.co.jemos.protomak.engine.impl.XsomXsdToProtoDomainConversionServiceImpl;
import uk.co.jemos.protomak.engine.test.utils.ProtomakEngineTestConstants;
import uk.co.jemos.protomak.engine.test.utils.ProtomakEngineTestHelper;
import uk.co.jemos.protomak.engine.utils.ProtomakEngineConstants;
import uk.co.jemos.protomak.engine.utils.ProtomakEngineHelper;

import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Unit Tests for the conversion of XSDs to Proto files.
 * 
 * @author mtedone
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(XSOMParser.class)
public class XsdToProtoDomainUnitTest {

	// ------------------->> Constants

	// ------------------->> Instance / Static variables

	private XsomXsdToProtoDomainConversionServiceImpl service;

	// ------------------->> Constructors

	// ------------------->> Public methods

	@Before
	public void init() {
		service = new XsomXsdToProtoDomainConversionServiceImpl();
		XSOMParser parser = new XSOMParser();
		service.setParser(parser);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConversionEngineForNonExistingXsdFile() {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.NON_EXISTING_FILE_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);
	}

	@Test(expected = ProtomakXsdToProtoConversionError.class)
	public void testConversionEngineWithIOException() throws Exception {

		File xsdSchema = new File(
				ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_XSD_PATH);

		XSOMParser parserMock = PowerMock.createMock(XSOMParser.class);

		service.setParser(parserMock);
		parserMock.setErrorHandler(EasyMock.isA(ErrorHandler.class));
		EasyMock.expectLastCall();
		parserMock.parse(xsdSchema);
		EasyMock.expectLastCall().andThrow(
				new IOException("Mocked IO exception"));

		PowerMock.replay(parserMock);

		try {

			service.generateProtoFiles(
					ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_XSD_PATH,
					ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		} finally {

			PowerMock.verify(parserMock);
		}

	}

	@Test(expected = ProtomakXsdToProtoConversionError.class)
	public void testConversionEngineWithSaxException() throws Exception {

		File xsdSchema = new File(
				ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_XSD_PATH);

		XSOMParser parserMock = PowerMock.createMock(XSOMParser.class);

		service.setParser(parserMock);
		parserMock.setErrorHandler(EasyMock.isA(ErrorHandler.class));
		parserMock.parse(xsdSchema);
		EasyMock.expectLastCall().andThrow(
				new SAXException("Mocked Sax exception"));

		PowerMock.replay(parserMock);

		try {

			service.generateProtoFiles(
					ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_XSD_PATH,
					ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		} finally {

			PowerMock.verify(parserMock);
		}

	}

	@Test
	public void testSimpleSingleElementXsd() throws Exception {
		service.generateProtoFiles(
				ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.SIMPLE_SINGLE_ELEMENT_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testSimpleMultipleElementsXsd() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.SIMPLE_MULTIPLE_ELEMENTS_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.SIMPLE_MULTIPLE_SIMPLE_ELEMENTS_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testSingleElementWithComplexType() throws Exception {
		service.generateProtoFiles(
				ProtomakEngineTestConstants.SINGLE_ELEMENT_WITH_COMPLEX_TYPE_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.SINGLE_ELEMENT_WITH_COMPLEX_TYPE_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testSingleElementWithInheritedComplexType() throws IOException {
		service.generateProtoFiles(
				ProtomakEngineTestConstants.SINGLE_ELEMENT_WITH_INHERITED_COMPLEX_TYPE_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.SINGLE_ELEMENT_WITH_INHERITED_COMPLEX_TYPE_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);
	}

	@Test
	public void testMultipleDepthInheritedComplexTypes() throws IOException {
		service.generateProtoFiles(
				ProtomakEngineTestConstants.MULTIPLE_DEPTH_INHERITED_COMPLEX_TYPES_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.MULTIPLE_DEPTH_INHERITED_COMPLEX_TYPES_XSD_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);
	}

	@Test
	public void testElementWithComplexAndSimpleTypeWithRestrictions()
			throws Exception {
		service.generateProtoFiles(
				ProtomakEngineTestConstants.ELEMENT_WITH_COMPLEX_AND_SIMPLE_TYPE_WITH_RESTRICTIONS_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.ELEMENT_COMPLEX_AND_SIMPLE_TYPE_WITH_RESTRICTIONS_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testSimpleOneLevelXsd() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.SIMPLE_ONE_LEVEL_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.SIMPLE_ONE_LEVEL_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testMultipleComplexTypes() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.MULTIPLE_COMPLEX_TYPES_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.MULTIPLE_COMPLEX_TYPES_ONLY_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testAnonymousTypes() throws Exception {
		service.generateProtoFiles(
				ProtomakEngineTestConstants.ANONYMOUS_TYPES_XSD_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.ANONYMOUS_TYPES_FILE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);
	}

	@Test
	public void testTopElementWithAnonymousComplexType() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.TOP_LEVEL_ELEMENT_WITH_ANONYMOUS_COMPLEX_TYPE_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.TOP_LEVEL_ELEMENT_WITH_ANONYMOUS_COMPLEX_TYPE_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}

	@Test
	public void testTopElementWithAnonymousComplexTypeWithChildrenBugPmk32() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.TOP_LEVEL_ELEMENT_WITH_ANONYMOUS_COMPLEX_TYPE_WITH_CHILDREN_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

		String protoFileName = ProtomakEngineHelper
				.extractProtoFileNameFromXsdName(ProtomakEngineTestConstants.TOP_LEVEL_ELEMENT_WITH_ANONYMOUS_COMPLEX_TYPE_WITH_CHILDREN_NAME);

		this.verifyExpectedAndActualProto(protoFileName);

	}
	
	@Test(expected = ProtomakXsdToProtoConversionError.class)
	public void testInvalidSchemaParsing() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.INVALID_SCHEMA_XDS_PATH,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);

	}

	@Test
	public void testXsdsToProto() throws Exception {

		service.generateProtoFiles(
				ProtomakEngineTestConstants.TEST_XSDS_FOLDER,
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR,
				ProtomakEngineConstants.XSD_EXTENSION);

		FilenameFilter protoFileNameFilter = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(
						ProtomakEngineConstants.PROTO_FILE_EXTENSION_NAME);
			}
		};

		FilenameFilter xsdFileNameFilter = new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(
						ProtomakEngineConstants.XSD_EXTENSION);
			}
		};

		File outputFolder = new File(
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR);
		Assert.assertTrue("The output folder must exist!",
				outputFolder.exists());

		File[] protoFiles = outputFolder.listFiles(protoFileNameFilter);
		Assert.assertNotNull("There are no output proto files!", protoFiles);
		Assert.assertTrue("There should be more than one proto file!",
				protoFiles.length > 1);

		File inputFolder = new File(
				ProtomakEngineTestConstants.TEST_XSDS_FOLDER);
		Assert.assertTrue("The input folder must exist!", inputFolder.exists());
		File[] xsdFiles = inputFolder.listFiles(xsdFileNameFilter);
		Assert.assertNotNull("The list of XSD files for input folder: "
				+ inputFolder.getAbsolutePath() + " is null!", xsdFiles);
		Assert.assertTrue(
				"There number of XSDs should at least be the same of that of protos",
				xsdFiles.length <= protoFiles.length);

	}

	// ------------------->> Getters / Setters

	// ------------------->> Private methods

	/**
	 * It verifies that the expected and actual proto files are the same.
	 * 
	 * @param protoFileName
	 *            The name of the proto file to verify.
	 * @throws IOException
	 *             If an IOException occurred while reading the streams.
	 */
	private void verifyExpectedAndActualProto(String protoFileName)
			throws IOException {

		Assert.assertNotNull("The expected proto file name cannot be null!",
				protoFileName);

		File expectedProtoFile = new File(
				ProtomakEngineTestConstants.EXPECTED_PROTO_DIR
						+ File.separatorChar + "expected-" + protoFileName);
		Assert.assertTrue(
				"The expected proto file "
						+ expectedProtoFile.getAbsolutePath()
						+ " does not exist!", expectedProtoFile.exists());

		String expectedProtoFileContent = ProtomakEngineTestHelper
				.retrieveFileContent(expectedProtoFile.getAbsolutePath());
		Assert.assertNotNull("The expected proto file content cannot be null!",
				expectedProtoFileContent);

		File actualProtoFile = new File(
				ProtomakEngineTestConstants.PROTOS_OUTPUT_DIR
						+ File.separatorChar + protoFileName);
		Assert.assertTrue(
				"The actual proto file: " + actualProtoFile.getAbsolutePath()
						+ " must exist!", actualProtoFile.exists());

		String actualProtoFileContent = ProtomakEngineTestHelper
				.retrieveFileContent(actualProtoFile.getAbsolutePath());
		Assert.assertNotNull("The actual proto file content must exist!",
				actualProtoFileContent);

		// Throws AssertionError if it fails
		ProtomakEngineTestHelper.compareExpectedAndActualProtos(
				expectedProtoFileContent, actualProtoFileContent);
	}

	// ------------------->> equals() / hashcode() / toString()

	// ------------------->> Inner classes

}
