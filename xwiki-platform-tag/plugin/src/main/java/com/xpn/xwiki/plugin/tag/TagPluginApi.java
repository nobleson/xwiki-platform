/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.xpn.xwiki.plugin.tag;

import java.util.List;
import java.util.Map;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.plugin.PluginApi;

/**
 * API for the TagPlugin.
 * 
 * TagPlugin is a plugin that allows to manipulate tags easily.
 * It allows to get, rename and delete tags.
 * 
 * @see PluginApi
 * @version $Id$
 */
public class TagPluginApi extends PluginApi<TagPlugin>
{
    /**
     * XWiki Plugin API constructor.
     * 
     * @param plugin The wrapped plugin.
     * @param context The current request context.
     * @see PluginApi#PluginApi(com.xpn.xwiki.plugin.XWikiPluginInterface, XWikiContext)
     */
    public TagPluginApi(TagPlugin plugin, XWikiContext context)
    {
        super(plugin, context);
    }
            
    /**
     * Get tags within the wiki.
     * 
     * @return list of tags.
     * @throws XWikiException if search query fails (possible failures: DB access problems, etc).
     * 
     */
    public List<String> getAllTags() throws XWikiException
    {
        return this.getProtectedPlugin().getAllTags(context);
    }
    
    /**
     * Get tags within the wiki with their occurences counts.
     * 
     * @return map of tags with their occurences counts.
     * @throws XWikiException if search query fails (possible failures: DB access problems, etc).
     * 
     */
    public Map<String, Integer> getTagCount() throws XWikiException
    {
        return this.getProtectedPlugin().getTagCount(context);
    }
    
    /**
     * Get tags within a specific space and their occurences counts.
     * 
     * @param space the space to get tags in
     * @return map of tags with their occurences counts
     * @throws XWikiException if search query fails (possible failures: DB access problems, etc).
     * @since 1.2
     */
    public Map<String, Integer> getTagCount(String space) throws XWikiException
    {
        return this.getProtectedPlugin().getTagCount(space, context);
    }
    
    /**
     * Get all the documents containing the given tag.
     * 
     * @param tag tag to match.
     * @return list of pages.
     * @throws XWikiException if search query fails (possible failures: DB access problems, etc).
     * 
     */
    public List<String> getDocumentsWithTag(String tag) throws XWikiException
    {
        return this.getProtectedPlugin().getDocumentsWithTag(tag, context);
    }

    /**
     * Get tags from a document. 
     *  
     * @param fullName name of the document.
     * @return list of tags.
     * @throws XWikiException if document read fails (possible failures: insufficient rights, DB access problems, etc).
     * 
     */
    public List<String> getTagsFromDocument(String fullName) throws XWikiException
    {
        return this.getProtectedPlugin().getTagsFromDocument(fullName, context);
    }
    
    /**
     * Add a tag to a document.
     * The document is saved (minor edit) after this operation.
     * 
     * @param tag tag to set.
     * @param fullName name of the document.
     * @return true if the tag has been added, false if the tag was already present.
     * @throws XWikiException if document save fails (possible failures: insufficient rights, DB access problems, etc). 
     * 
     */
    public boolean addTagToDocument(String tag, String fullName)  throws XWikiException
    {
        return this.getProtectedPlugin().addTagToDocument(tag, fullName, context);
    }
    
    /**
     * Remove a tag from a document.
     * The document is saved (minor edit) after this operation.
     * 
     * @param tag tag to remove.
     * @param fullName name of the document.
     * @return true if the tag has been removed, false if the tag was not present.
     * @throws XWikiException if document save fails (possible failures: insufficient rights, DB access problems, etc). 
     * 
     */
    public boolean removeTagFromDocument(String tag, String fullName) throws XWikiException
    {
        return this.getProtectedPlugin().removeTagFromDocument(tag, fullName, context);
    }
    
    /**
     * Rename a tag in all the documents that contains it. Requires admin rights.
     * Document containing this tag are saved (minor edit) during this operation.
     * 
     * @param tag tag to rename.
     * @param newTag new tag.
     * @return true if the rename has succeeded.
     * @throws XWikiException if document save fails (possible failures: insufficient rights, DB access problems, etc).
     * 
     */
    public boolean renameTag(String tag, String newTag) throws XWikiException
    {
        if (hasAdminRights()) {
            return this.getProtectedPlugin().renameTag(tag, newTag, context);
        } else {
            return false;
        }
    }
    
    /**
     * Delete a tag from all the documents that contains it. Requires admin rights.
     * Document containing this tag are saved (minor edit) during this operation.
     * 
     * @param tag tag to delete.
     * @return true if the delete has succeeded.
     * @throws XWikiException if document save fails (possible failures: insufficient rights, DB access problems, etc).
     * 
     */
    public boolean deleteTag(String tag)  throws XWikiException
    {        
        if (hasAdminRights()) {
            return this.getProtectedPlugin().deleteTag(tag, context);
        } else {  
            return false;
        }
    }
}
