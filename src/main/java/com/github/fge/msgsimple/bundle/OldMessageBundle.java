package com.github.fge.msgsimple.bundle;

import com.github.fge.msgsimple.source.MessageSource;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Simple, non-localized message bundle
 *
 * <p>In order to create a bundle, you need to perform the following steps:</p>
 *
 * <ul>
 *     <li>create one or more {@link MessageSource}s;</li>
 *     <li>create a new bundle builder, using {@link
 *     OldMessageBundle#newBundle()}</li>;
 *     <li>append/prepend your message sources using {@link
 *     OldMessageBundle.Builder#appendSource(MessageSource)} or {@link
 *     OldMessageBundle.Builder#prependSource(MessageSource)};</li>
 *     <li>build the final bundle using {@link
 *     OldMessageBundle.Builder#build()}.</li>
 * </ul>
 *
 * <p>You can also reuse an existing bundle and modify it to suit your needs,
 * using the {@link #modify()} method. Note that this will return a builder;
 * once you {@link OldMessageBundle.Builder#build()} it again, this will be a
 * <b>new</b> bundle, the original one will be left intact.</p>
 */
@ThreadSafe
public final class OldMessageBundle
{
    private final List<MessageSource> sources;

    private OldMessageBundle(final Builder builder)
    {
        sources = new ArrayList<MessageSource>(builder.sources);
    }

    /**
     * Get the message matching that key
     *
     * <p>This method looks up all declared message sources for a string
     * matching this key. If the given key was not found in any message source,
     * the key itself is returned.</p>
     *
     * <p>This is therefore a very different behaviour from what you would
     * expect from a {@link ResourceBundle}, which throws an (unchecked!)
     * exception if the key cannot be found. This also means that this method
     * <b>never</b> returns {@code null}.</p>
     *
     * @param key the key to return
     * @return the first matching message for that key; the key itself if no
     * source has a matching entry for this key
     * @throws NullPointerException key is null
     * @see MessageSource
     * @see OldMessageBundle.Builder
     */
    public String getKey(final String key)
    {
        if (key == null)
            throw new NullPointerException("cannot query null key");

        String ret;

        for (final MessageSource source: sources) {
            ret = source.getKey(key);
            if (ret != null)
                return ret;
        }

        return key;
    }

    /**
     * Return a modifable version of this bundle
     *
     * @return a {@link Builder} with this bundle's message sources
     * @since 0.2
     */
    public Builder modify()
    {
        return new Builder(this);
    }

    /**
     * Return a modifable version of this bundle
     *
     * @return a {@link Builder} with this bundle's message sources
     * @deprecated use {@link #modify()} instead. Will be gone in 0.3.
     */
    @Deprecated
    public Builder copy()
    {
        return modify();
    }

    /**
     * Create a new, empty bundle builder
     *
     * @return a {@link Builder}
     * @since 0.2
     */
    public static Builder newBundle()
    {
        return new Builder();
    }

    @NotThreadSafe
    public static final class Builder
    {
        private final List<MessageSource> sources
            = new ArrayList<MessageSource>();

        /**
         * Constructor
         *
         * @deprecated use {@link #newBundle()} instead. Will be gone in 0.3.
         */
        @Deprecated
        public Builder()
        {
        }

        private Builder(final OldMessageBundle bundle)
        {
            sources.addAll(bundle.sources);
        }

        /**
         * Append one message source to the already registered sources
         *
         * @param source the source to append
         * @throws NullPointerException source is null
         * @return this
         */
        public Builder appendSource(final MessageSource source)
        {
            if (source == null)
                throw new NullPointerException("cannot append " +
                    "null message source");
            sources.add(source);
            return this;
        }

        /**
         * Prepend one message source to the already registered soruces
         *
         * @param source the source to prepend
         * @throws NullPointerException source is null
         * @return this
         */
        public Builder prependSource(final MessageSource source)
        {
            if (source == null)
                throw new NullPointerException("cannot prepend " +
                    "null message source");
            sources.add(0, source);
            return this;
        }

        /**
         * Build the bundle
         *
         * @return a newly created {@link OldMessageBundle}
         */
        public OldMessageBundle build()
        {
            return new OldMessageBundle(this);
        }
    }
}