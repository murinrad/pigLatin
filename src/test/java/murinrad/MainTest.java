package murinrad;

import java.io.FileReader;
import murinrad.Main.CasePreserver;
import murinrad.Main.CasePreserver.MidWordPunctuationPreserver;
import murinrad.Main.PigLatinApplicator;
import murinrad.Main.WordTokenizerApplicator;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class MainTest {

  PigLatinApplicator pigLatinApplicator = new PigLatinApplicator();
  CasePreserver casePreserver = new CasePreserver();
  MidWordPunctuationPreserver punctiationPreserver = new MidWordPunctuationPreserver();
  WordTokenizerApplicator wordTokenizerApplicator = new WordTokenizerApplicator();

  @Test
  public void applyPigLatin() {
    Assert.assertEquals("ellohay", pigLatinApplicator.apply("hello"));
    Assert.assertEquals("appleway", pigLatinApplicator.apply("apple"));
    Assert.assertEquals("stairway", pigLatinApplicator.apply("stairway"));
    Assert.assertEquals("day", pigLatinApplicator.apply("d"));
    Assert.assertEquals("away", pigLatinApplicator.apply("a"));
    Assert.assertEquals("", pigLatinApplicator.apply(null));
  }

  @Test
  public void applyCasePreserver() {

    Assert.assertEquals("EllOhay", casePreserver.apply("HelLo"));
    Assert.assertEquals("aPPleway", casePreserver.apply("aPPle"));
    Assert.assertEquals("StAiRWaY", casePreserver.apply("StAiRWaY"));
    Assert.assertEquals("", casePreserver.apply(null));

  }

  @Test
  public void punctuationPreserver() {
    Assert.assertEquals("antca’y", punctiationPreserver.apply("can’t"));
    Assert.assertEquals("StAiRWaY", punctiationPreserver.apply("StAiRWaY"));
    Assert.assertEquals("histay", punctiationPreserver.apply("this"));
    Assert.assertEquals("hingtay", punctiationPreserver.apply("thing"));
    Assert.assertEquals("", punctiationPreserver.apply( null));
  }

  @Test
  public void wordTokenizer() {
    Assert.assertEquals("histay-hingtay", wordTokenizerApplicator.apply("this-thing"));
    Assert.assertEquals("histay-", wordTokenizerApplicator.apply("this-"));
    Assert.assertEquals("-histay", wordTokenizerApplicator.apply("-this"));
    Assert.assertEquals("antca’y", wordTokenizerApplicator.apply("can’t"));
    Assert.assertEquals("StAiRWaY", wordTokenizerApplicator.apply("StAiRWaY"));
    Assert.assertEquals("histay", wordTokenizerApplicator.apply("this"));
    Assert.assertEquals("hingtay", wordTokenizerApplicator.apply("thing"));
    Assert.assertEquals("Eachbay", wordTokenizerApplicator.apply("Beach"));
    Assert.assertEquals("CcLoudmay", wordTokenizerApplicator.apply("McCloud"));
    Assert.assertEquals("endway.", wordTokenizerApplicator.apply("end."));
    Assert.assertEquals("enway.day", wordTokenizerApplicator.apply("en.d"));
    Assert.assertEquals("", wordTokenizerApplicator.apply(""));
    Assert.assertEquals("", wordTokenizerApplicator.apply(null));
  }
}