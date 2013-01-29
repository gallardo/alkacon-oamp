/*
 * File   : $Source: /alkacon/cvs/alkacon/com.alkacon.opencms.photoalbum/src/com/alkacon/opencms/photoalbum/CmsPhotoAlbumBean.java,v $
 * Date   : $Date: 2008/06/18 10:56:23 $
 * Version: $Revision: 1.3 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2005 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.alkacon.opencms.v8.photoalbum;

import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.file.types.CmsResourceTypeImage;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.loader.CmsImageScaler;
import org.opencms.main.CmsException;
import org.opencms.util.CmsStringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;

/**
 * Contains helper functions to show the photo album.<p>
 * 
 * @author Peter Bonrad
 * @author Ruediger Kurz
 * 
 * @version $Revision: 1.3 $
 * 
 * @since 8.0.2
 */
public class CmsPhotoAlbumBean extends CmsJspActionElement {

    /** The image scaler to check if a downscale is required. */
    protected CmsImageScaler m_imageScaler;

    /** Lazy map with the list of resources (images) for a path. */
    private Map<CmsResource, Boolean> m_downscaleRequired;

    /** Lazy map with the list of resources (images) for a path. */
    private Map<String, List<CmsResource>> m_images;

    /**
     * Empty constructor, required for every JavaBean.<p>
     */
    public CmsPhotoAlbumBean() {

        super();
    }

    /**
     * Constructor, with parameters.<p>
     * 
     * @param context the JSP page context object
     * @param req the JSP request 
     * @param res the JSP response 
     */
    public CmsPhotoAlbumBean(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        super();
        init(context, req, res);
    }

    /**
     * Returns a lazy initialized map that checks if downscaling is required
     * for the given resource used as a key in the Map.<p> 
     * 
     * @return a lazy initialized map
     */
    @SuppressWarnings("unchecked")
    public Map<CmsResource, Boolean> getIsDownscaleRequired() {

        if (m_downscaleRequired == null) {
            m_downscaleRequired = LazyMap.decorate(new HashMap<CmsResource, Boolean>(), new Transformer() {

                /**
                 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
                 */
                public Object transform(Object input) {

                    Boolean result = Boolean.FALSE;
                    if (m_imageScaler == null) {
                        return result;
                    }

                    CmsImageScaler scaler = new CmsImageScaler(getCmsObject(), (CmsResource)input);
                    if (scaler.isDownScaleRequired(m_imageScaler)) {
                        return Boolean.TRUE;
                    }
                    return result;
                }
            });
        }
        return m_downscaleRequired;
    }

    /**
     * Returns a lazy initialized map that provides the list of resources (images)
     * for each path to an image gallery used as a key in the Map.<p> 
     * 
     * @return a lazy initialized map
     */
    @SuppressWarnings("unchecked")
    public Map<String, List<CmsResource>> getReadImages() {

        if (m_images == null) {
            m_images = LazyMap.decorate(new HashMap<String, List<CmsResource>>(), new Transformer() {

                /**
                 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
                 */
                public Object transform(Object input) {

                    List<CmsResource> result = null;
                    try {
                        result = getCmsObject().readResources(
                            (String)input,
                            CmsResourceFilter.DEFAULT.addRequireType(CmsResourceTypeImage.getStaticTypeId()),
                            true);
                    } catch (CmsException ex) {
                        // noop
                    }
                    return result;
                }
            });
        }
        return m_images;
    }

    /**
     * @see org.opencms.jsp.CmsJspBean#init(javax.servlet.jsp.PageContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void init(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        String imgSize = req.getParameter("maxImageSize");
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(imgSize)) {
            m_imageScaler = new CmsImageScaler();
            String[] values = imgSize.split("x");
            try {
                m_imageScaler.setWidth(Integer.parseInt(values[0]));
                m_imageScaler.setHeight(Integer.parseInt(values[1]));
            } catch (NumberFormatException ex) {
                m_imageScaler = null;
            }
        }

        super.init(context, req, res);
    }
}
