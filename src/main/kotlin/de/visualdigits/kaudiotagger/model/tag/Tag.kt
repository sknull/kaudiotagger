package de.visualdigits.kaudiotagger.model.tag

/**
 * This interface represents the basic data structure for the default
 * audio library functionality.<br>
 * <p>
 * Some audio file tagging systems allow to specify multiple values for one type
 * of information. The artist for example. Some songs may be a cooperation of
 * two or more artists. Sometimes a tagging user wants to specify them in the
 * tag without making one long text string.<br>
 * <p>
 * The addField() method can be used for this but it is possible the underlying implementation
 * does not support that kind of storing multiple values and will just overwrite the existing value<br>
 * <br>
 * <b>Code Examples:</b><br>
 *
 * <pre>
 * <code>
 * AudioFile file = AudioFileIO.read(new File(&quot;C:\\test.mp3&quot;));
 *
 * Tag tag = file.getTag();
 * </code>
 * </pre>
 *
 * @author Raphael Slinckx
 * @author Paul Taylor
 */
interface Tag {
}