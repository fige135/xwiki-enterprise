package com.xpn.xwiki.it.selenium;

import junit.framework.Test;

import com.xpn.xwiki.it.selenium.framework.AbstractXWikiTestCase;
import com.xpn.xwiki.it.selenium.framework.AlbatrossSkinExecutor;
import com.xpn.xwiki.it.selenium.framework.XWikiTestSuite;

/**
 * Tests the document edit section feature
 * 
 * @version $Id$
 */
public class SectionTest extends AbstractXWikiTestCase
{
    public static Test suite()
    {
        XWikiTestSuite suite = new XWikiTestSuite("Tests the document edit section feature");
        suite.addTestSuite(SectionTest.class, AlbatrossSkinExecutor.class);
        return suite;
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        loginAsAdmin();
    }

    /**
     * Verify edit section is working in wiki editor (xwiki/1.0).
     * XWIKI-174 : Sectional editing.
     */
    public void testSectionEditInWikiEditor()
    {
        open("Sandbox", "WebHome", "edit", "editor=wiki");        
        createPage("Test", "SectionEditing", "1 First section\nSection 1 content\n\n"
            + "1 Second section\nSection 2 content\n\n1.1 Subsection\nSubsection content\n\n"
            + "1 Third section\nSection 3 content", "xwiki/1.0");        
        clickLinkWithLocator("//div[@id='xwikicontent']/span[2]/a"); // Edit the second section
        clickLinkWithText("Wiki");
        assertTextNotPresent("First section");
        assertTextPresent("Second section");
        assertTextPresent("Subsection");
        assertTextNotPresent("Third section");
    }

    /**
     * Verify edit section is working in wysiwyg editor (xwiki/1.0).
     * XWIKI-174 : Sectional editing.
     */
    public void testSectionEditInWysiwygEditor()
    {
        open("Test", "SectionEditing");
        clickLinkWithLocator("//div[@id='xwikicontent']/span[3]/a"); // Edit the subsection
        assertTextNotPresent("First section");
        assertTextNotPresent("Second section");
        assertTextPresent("Subsection");
        assertTextNotPresent("Third section");        
    }
    
    /**
     * Verify section save does not override the whole document content (xwiki/1.0).
     * XWIKI-4033: When saving after section edit entire page is overwritten.
     */
    public void testSectionSaveDoesNotOverrideTheWholeContent()
    {
        open("Test", "SectionEditing");
        clickLinkWithLocator("//div[@id='xwikicontent']/span[3]/a"); // Edit the subsection
        clickLinkWithText("Wiki");
        clickEditSaveAndView();
        assertTextPresent("First section");
        assertTextPresent("Second section");
        assertTextPresent("Subsection");
        assertTextPresent("Third section");
        deletePage("Test", "SectionEditing");
    }

    /**
     * Verify edit section is working in wiki editor (xwiki/2.0).
     * XWIKI-2881 : Implement Section editing.
     */
    public void testSectionEditInWikiEditor_syntax20()
    {
        createPage("Test", "SectionEditingIncluded", 
            "== Included section ==\nFirst Included section content\n{{velocity wiki=true}}\n"
                + "#foreach($h in ['First', 'Second'])\n== $h generated section ==\n\n$h generated paragraph\n"
                + "#end\n{{velocity}}\n");
        createPage("Test", "SectionEditing20", "= First section =\nSection 1 content\n\n"
            + "= Second section =\nSection 2 content\n\n== Subsection ==\nSubsection content\n\n"
            + "{{include document='Test.SectionEditingIncluded'/}}\n\n" + "= Third section =\nSection 3 content",
            "xwiki/2.0");    
        clickLinkWithLocator("//div[@id='xwikicontent']/span[2]/a"); // Edit the second section
        clickLinkWithText("Wiki");
        assertTextNotPresent("First section");
        assertTextPresent("Second section");
        assertTextPresent("Subsection");
        assertTextNotPresent("Third section");
    }

    /**
     * Verify edit section is working in wysiwyg editor (xwiki/2.0).
     * XWIKI-2881 : Implement Section editing.
     */
    public void testSectionEditInWysiwygEditor_syntax20()
    {
        open("Test", "SectionEditing20");        
        clickLinkWithLocator("//div[@id='xwikicontent']/span[4]/a"); // Edit the last section
        assertTextNotPresent("First section");
        assertTextNotPresent("Second section");
        assertTextNotPresent("Subsection");
        assertTextPresent("Third section");        
    }
    
    /**
     * Verify section save does not override the whole document content (xwiki/2.0).
     * XWIKI-4033: When saving after section edit entire page is overwritten.
     */
    public void testSectionSaveDoesNotOverrideTheWholeContent_syntax20()
    {        
        open("Test", "SectionEditing20");
        clickLinkWithLocator("//div[@id='xwikicontent']/span[4]/a"); // Edit the last section
        clickLinkWithText("Wiki");
        clickEditSaveAndView();
        assertTextPresent("First section");
        assertTextPresent("Second section");
        assertTextPresent("Third section");
        deletePage("Test", "SectionEditing20");
        deletePage("Test", "SectionEditingIncluded");
    }
}
