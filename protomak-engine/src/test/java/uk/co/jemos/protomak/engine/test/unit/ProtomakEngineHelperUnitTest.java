/**
 * 
 */
package uk.co.jemos.protomak.engine.test.unit;

import junit.framework.Assert;

import org.junit.Test;

import uk.co.jemos.protomak.engine.test.utils.ProtomakEngineTestConstants;
import uk.co.jemos.protomak.engine.utils.ProtomakEngineConstants;
import uk.co.jemos.protomak.engine.utils.ProtomakEngineHelper;
import uk.co.jemos.xsds.protomak.proto.MessageAttributeOptionalType;

/**
 * Unit tests for the {@link ProtomakEngineHelper} class.
 * 
 * @author mtedone
 * 
 */
public class ProtomakEngineHelperUnitTest {

	// ------------------->> Constants

	// ------------------->> Instance / Static variables

	// ------------------->> Constructors

	// ------------------->> Public methods

	@Test(expected = IllegalArgumentException.class)
	public void testTargetNameSpaceToProtoPackageConversionForNullString() {
		ProtomakEngineHelper.convertTargetNsToProtoPackageName(null);
	}

	@Test
	public void testTargetNsToProtoPackageConversion() {

		String expectedPackageName = "eu.jemos.www.simple_one_level;";

		String packageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(ProtomakEngineTestConstants.TEST_TARGET_NAMESPACE_WITH_HTTP_PREFIX);
		this.verifyPackageName(expectedPackageName, packageName);

	}

	@Test
	public void testTargetNsToProtoPackageConversionWithSimpleName() {
		String expectedPackageName = "foo";
		String packageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(expectedPackageName);
		this.verifyPackageName(expectedPackageName + ";", packageName);
	}

	@Test
	public void testTargetNsToProtoPackageConversionWithSimpleNameWithDots() {
		String expectedPackageName = "foo.bar.baz";
		String packageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(expectedPackageName);
		this.verifyPackageName(expectedPackageName + ";", packageName);
	}

	@Test
	public void testTargetNsToProtoPackageConversionWithSimpleNameWithSlashes() {
		String expectedPackageName = "foo.bar.baz";
		String packageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName("foo/bar/baz");
		this.verifyPackageName(expectedPackageName + ";", packageName);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTargetNsToProtoPackageConversionWithPackageNameEndingWithDot() {
		ProtomakEngineHelper.convertTargetNsToProtoPackageName("foo.");
	}

	@Test
	public void testTargetNsToProtoPackageConversionWithPackageNameStartingWithDot() {
		String expectedPackageName = "foo";
		String actualPackageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(".foo");
		this.verifyPackageName(expectedPackageName + ";", actualPackageName);
	}

	@Test
	public void testTargetNsToProtoPackageConversionWithPackageNameStartingWithNumber() {
		for (int i = 0; i < 10; i++) {
			try {
				ProtomakEngineHelper.convertTargetNsToProtoPackageName(i
						+ "foo");
				Assert.fail("The method for " + i
						+ "foo should have thrown an exception");
			} catch (IllegalArgumentException e) {
				// OK, expected
			}
		}

	}

	@Test
	public void testTargetNsWithUpperCasesToProtoPackageConversion() {

		String expectedPackageName = "eu.jemos.www.foo;";

		String packageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(ProtomakEngineTestConstants.TEST_TARGET_NAMESPACE_WITH_HTTP_PREFIX_AND_SOME_UPPERCASE);
		this.verifyPackageName(expectedPackageName, packageName);

	}

	@Test
	public void testTargetNsWithRelativeUrl() {

		String expectedPackageName = "foo.bar.baz.my_namespace;";

		String packageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(ProtomakEngineTestConstants.TEST_TARGET_NAMESPACE_WITH_RELATIVE_URL);
		this.verifyPackageName(expectedPackageName, packageName);

	}

	@Test
	public void testPackageNameFromOpaqueUri() {

		String expectedPackageName = ProtomakEngineConstants.PROTOMAK_DEFAULT_PACKAGE_NAME
				+ ";";

		String packageName = "urn:com/example/test";
		this.checkOpaqueUrlMatches(expectedPackageName, packageName);
		packageName = "mailto:foo@bar.baz";
		this.checkOpaqueUrlMatches(expectedPackageName, packageName);
		packageName = "news:comp.lang.java";
		this.checkOpaqueUrlMatches(expectedPackageName, packageName);

	}

	private void checkOpaqueUrlMatches(String expectedPackageName,
			String packageName) {
		String actualPackageName = ProtomakEngineHelper
				.convertTargetNsToProtoPackageName(packageName);
		Assert.assertNotNull("The actual package name cannot be null!",
				actualPackageName);
		Assert.assertTrue(
				"The actual package name cannot be an empty string ending with ';'!",
				!";".equals(actualPackageName));
		Assert.assertEquals(
				"The actual and expected package names don't match",
				expectedPackageName, actualPackageName);
	}

	@Test
	public void testOptionality() {

		int minOccurs = 0;
		int maxOccurs = 1;

		MessageAttributeOptionalType type = ProtomakEngineHelper
				.getMessageAttributeOptionality(minOccurs, maxOccurs);
		Assert.assertEquals(
				"The message attribute optional type does not match the expected one!",
				MessageAttributeOptionalType.OPTIONAL, type);

		minOccurs = 0;
		maxOccurs = -1;
		type = ProtomakEngineHelper.getMessageAttributeOptionality(minOccurs,
				maxOccurs);
		Assert.assertEquals(
				"The message attribute optional type does not match the expected one!",
				MessageAttributeOptionalType.REPEATED, type);

		minOccurs = 1;
		maxOccurs = 1;
		type = ProtomakEngineHelper.getMessageAttributeOptionality(minOccurs,
				maxOccurs);
		Assert.assertEquals(
				"The message attribute optional type does not match the expected one!",
				MessageAttributeOptionalType.REQUIRED, type);

		minOccurs = 1;
		maxOccurs = -1;
		type = ProtomakEngineHelper.getMessageAttributeOptionality(minOccurs,
				maxOccurs);
		Assert.assertEquals(
				"The message attribute optional type does not match the expected one!",
				MessageAttributeOptionalType.REPEATED, type);

		// Now let's test some negative scenarios
		maxOccurs = 1;
		for (int i = -1; i < 3; i++) {
			minOccurs = i;
			if (minOccurs < 0 || minOccurs > 1) {
				try {
					ProtomakEngineHelper.getMessageAttributeOptionality(
							minOccurs, maxOccurs);
					Assert.fail("The method should have failed since minOccurs is: "
							+ minOccurs);
				} catch (IllegalArgumentException e) {
					// OK, expected
				}
			}
		}

		minOccurs = 0;
		for (int i = -2; i < 3; i++) {
			maxOccurs = i;
			if (maxOccurs < -1 || maxOccurs > 1) {
				try {
					ProtomakEngineHelper.getMessageAttributeOptionality(
							minOccurs, maxOccurs);
					Assert.fail("The method should have failed since maxOccurs is: "
							+ maxOccurs);
				} catch (IllegalArgumentException e) {
					// OK, expected
				}
			}
		}

	}

	// ------------------->> Getters / Setters

	// ------------------->> Private methods

	/**
	 * It verifies that the package name matches the expectations.
	 * 
	 * @param expectedPackageName
	 *            The expected package name
	 * @param packageName
	 *            The actual package name
	 */
	private void verifyPackageName(String expectedPackageName,
			String packageName) {
		Assert.assertNotNull("The package name cannot be null!", packageName);
		Assert.assertEquals(
				"The expected and actual proto package name don't match",
				expectedPackageName, packageName);
	}

	// ------------------->> equals() / hashcode() / toString()

	// ------------------->> Inner classes

}
