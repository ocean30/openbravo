/*
 *************************************************************************
 * The contents of this file are subject to the Openbravo  Public  License
 * Version  1.1  (the  "License"),  being   the  Mozilla   Public  License
 * Version 1.1  with a permitted attribution clause; you may not  use this
 * file except in compliance with the License. You  may  obtain  a copy of
 * the License at http://www.openbravo.com/legal/license.html 
 * Software distributed under the License  is  distributed  on  an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific  language  governing  rights  and  limitations
 * under the License. 
 * The Original Code is Openbravo ERP. 
 * The Initial Developer of the Original Code is Openbravo SLU 
 * All portions are Copyright (C) 2008-2010 Openbravo SLU 
 * All Rights Reserved. 
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

package org.openbravo.service.system;

import java.io.File;

import org.apache.log4j.Logger;
import org.openbravo.base.exception.OBException;
import org.openbravo.base.session.OBPropertiesProvider;

/**
 * Restarts the tomcat using the direct java bootstrap class/jar. The restart is an ant task which
 * itself calls another ant task. The reason that the first ant task is required because the ant
 * task needs to be spawned and only a java or exec task can be spawned. The stop and restart action
 * are two java tasks which should be done in one step. Therefore these two actions have to be
 * combined into one task which is called by the first (spawned) java task.
 * 
 * @author mtaal
 */
public class RestartTomcat {
  private static final Logger log = Logger.getLogger(RestartTomcat.class);

  /**
   * Method is called from the tomcat.restart tasks, this method again starts the tomcat.restart.do
   * task.Note: this method should always use the AntExecutor in org.openbravo.erpCommon.utility, to
   * prevent problems when upgrading Core to a newer version
   * 
   * @param args
   *          arg[0] contains the source path
   * @throws Exception
   */
  @SuppressWarnings("deprecation")
  public static void main(String[] args) throws Exception {
    final String srcPath = args[0];
    final File srcDir = new File(srcPath);
    final File baseDir = srcDir.getParentFile();
    try {
      log.debug("Restarting tomcat with basedir " + baseDir);
      final org.openbravo.erpCommon.utility.AntExecutor antExecutor = new org.openbravo.erpCommon.utility.AntExecutor(
          baseDir.getAbsolutePath());
      antExecutor.runTask("tomcat.restart.do");
    } catch (final Exception e) {
      throw new OBException(e);
    }
  }

  /**
   * Restarts the tomcat server. Assumes the the Openbravo.properties are available through the
   * {@link OBPropertiesProvider}. Note: this method should always use the AntExecutor in
   * org.openbravo.erpCommon.utility, to prevent problems when upgrading Core to a newer version
   */
  @SuppressWarnings("deprecation")
  public static void restart() {
    final String baseDirPath = OBPropertiesProvider.getInstance().getOpenbravoProperties()
        .getProperty("source.path");
    try {
      log.debug("Restarting tomcat with basedir " + baseDirPath);
      final org.openbravo.erpCommon.utility.AntExecutor antExecutor = new org.openbravo.erpCommon.utility.AntExecutor(
          baseDirPath);
      antExecutor.runTask("tomcat.restart");
    } catch (final Exception e) {
      throw new OBException(e);
    }
  }
}
