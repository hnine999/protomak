/**
 * 
 */
package uk.co.jemos.protomak.engine.impl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import uk.co.jemos.protomak.engine.exceptions.ProtomakXsdToProtoConversionError;

/**
 * It manages errors occurred while parsing.
 * 
 * @author mtedone
 * 
 */
public class ProtomakErrorHandler implements ErrorHandler {

	// ------------------->> Constants

	/** The application logger */
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(ProtomakErrorHandler.class);

	// ------------------->> Instance / Static variables

	// ------------------->> Constructors

	// ------------------->> Public methods

	/**
	 * {@inheritDoc}
	 */
	public void warning(SAXParseException exception) throws SAXException {
		LOG.warn("Warning while parsing the schema...", exception);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ProtomakXsdToProtoConversionError
	 *             If an error occurred while parsing the schema.
	 */
	public void error(SAXParseException exception) throws SAXException {
		LOG.error("Error while parsing the schema...", exception);
		throw new ProtomakXsdToProtoConversionError(exception);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ProtomakXsdToProtoConversionError
	 *             If an error occurred while parsing the schema.
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		LOG.fatal("Fatal error while parsing the schema...", exception);
		throw new ProtomakXsdToProtoConversionError(exception);
	}

	// ------------------->> Getters / Setters

	// ------------------->> Private methods

	// ------------------->> equals() / hashcode() / toString()

	// ------------------->> Inner classes

}
