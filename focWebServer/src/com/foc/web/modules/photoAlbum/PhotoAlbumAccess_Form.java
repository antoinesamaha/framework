/*******************************************************************************
 * Copyright 2016 Antoine Nicolas SAMAHA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.foc.web.modules.photoAlbum;

import java.io.InputStream;

import com.foc.Globals;
import com.foc.business.photoAlbum.PhotoAlbum;
import com.foc.business.photoAlbum.PhotoAlbumDesc;
import com.foc.business.photoAlbum.PhotoAlbumListWithFilter;
import com.foc.desc.FocObject;
import com.foc.list.FocList;
import com.foc.property.FCloudStorageProperty;
import com.foc.shared.xmlView.XMLViewKey;
import com.foc.vaadin.FocWebApplication;
import com.foc.vaadin.gui.components.BlobResource;
import com.foc.vaadin.gui.components.FVTablePopupMenu;
import com.foc.vaadin.gui.components.upload.FVImageReceiver;
import com.foc.vaadin.gui.components.upload.FVUpload_Image;
import com.foc.vaadin.gui.layouts.FVTableWrapperLayout;
import com.foc.vaadin.gui.xmlForm.FocXMLLayout;
import com.foc.web.server.xmlViewDictionary.XMLViewDictionary;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.SucceededEvent;

@SuppressWarnings("serial")
public class PhotoAlbumAccess_Form extends FocXMLLayout {

}
