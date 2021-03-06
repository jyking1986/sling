/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.ide.eclipse.core.internal;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ProjectHelper {

	public static boolean isPotentialBundleProject(IProject project) {
		String packaging = getMavenProperty(project, "packaging");
		return (packaging!=null && "bundle".equals(packaging));
	}
	
	public static boolean isPotentialContentProject(IProject project) {
		String packaging = getMavenProperty(project, "packaging");
		return (packaging!=null && "content-package".equals(packaging));
	}
	
	public static String getMavenProperty(IProject project, String name) {
		try{
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			IFile file = project.getFile("pom.xml");
			if (file==null || !file.exists()) {
				return null;
			}
			Document document = docBuilder.parse(file.getContents());
			Element docElement = document.getDocumentElement();
			NodeList children = docElement.getChildNodes();
			for(int i=0; i<children.getLength(); i++) {
				Node aChild = children.item(i);
				if (aChild.getNodeName().equals(name)) {
					Element e = (Element) aChild;
					String text = e.getTextContent();
					return text;
				}
			}
		} catch (ParserConfigurationException e) {
			//TODO proper logging
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			//TODO proper logging
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			//TODO proper logging
			e.printStackTrace();
			return null;
		} catch (CoreException e) {
			//TODO proper logging
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public static boolean isBundleProject(IProject project) {
		return containsFacet(project, SlingBundleModuleFactory.SLING_BUNDLE_FACET_ID);
	}

	public static boolean isContentProject(IProject project) {
		return containsFacet(project, SlingContentModuleFactory.SLING_CONTENT_FACET_ID);
	}

	private static boolean containsFacet(IProject project, String facetId) {
        // deleted modules can trigger a publish call without having an attached project
        if (project == null) {
            return false;
        }
		IFacetedProject facetedProject = (IFacetedProject) project.getAdapter(IFacetedProject.class);
		if (facetedProject==null ) {
			return false;
		}
		IProjectFacet facet = ProjectFacetsManager.getProjectFacet(facetId);
		return facetedProject.hasProjectFacet(facet);
	}
	
	public static IJavaProject asJavaProject(IProject project) {
		return JavaCore.create(project);
	}
	
	static IJavaProject[] getAllJavaProjects() {
		IJavaModel model = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		IJavaProject[] jps;
		try {
			jps = model.getJavaProjects();
			return jps;
		} catch (JavaModelException e) {
			e.printStackTrace();
			return null;
		}
	}

}
