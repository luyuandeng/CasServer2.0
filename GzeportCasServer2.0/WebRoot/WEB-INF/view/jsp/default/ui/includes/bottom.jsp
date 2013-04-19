<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
                </div>
                <div id="footer" class="fl-panel fl-note fl-bevel-white fl-font-size-80">
                	<a id="jasig" href="http://www.jasig.org" title="go to Jasig home page"></a>
                    <div id="copyright">
                      	<div id="sidebar">
			                <div id="list-languages" class="fl-panel">
			                <%final String queryString = request.getQueryString() == null ? "" : request.getQueryString().replaceAll("&locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]|^locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]", "");%>
								<c:set var='query' value='<%=queryString%>' />
			                    <c:set var="xquery" value="${fn:escapeXml(query)}" />
			                  <h3>Languages:</h3>
			                  <c:choose>
			                     <c:when test="${not empty requestScope['isMobile'] and not empty mobileCss}">
			                        <form method="get" action="login?${xquery}">
			                           <select name="locale">
			                               <option value="en">English</option>
			                               <option value="es">Spanish</option>
			                               <option value="fr">French</option>
			                               <option value="ru">Russian</option>
			                               <option value="nl">Nederlands</option>
			                               <option value="sv">Svenskt</option>
			                               <option value="it">Italiano</option>
			                               <option value="ur">Urdu</option>
			                               <option value="zh_CN">Chinese (Simplified)</option>
			                               <option value="de">Deutsch</option>
			                               <option value="ja">Japanese</option>
			                               <option value="hr">Croatian</option>
			                               <option value="cs">Czech</option>
			                               <option value="sl">Slovenian</option>
			                               <option value="pl">Polish</option>
			                               <option value="ca">Catalan</option>
			                               <option value="mk">Macedonian</option>
			                           </select>
			                           <input type="submit" value="Switch">
			                        </form>
			                     </c:when>
			                     <c:otherwise>
			                        <c:set var="loginUrl" value="login?${xquery}${not empty xquery ? '&' : ''}locale=" />
									<ul
										><li class="first"><a href="${loginUrl}en">English</a></li
										><li><a href="${loginUrl}es">Spanish</a></li
										><li><a href="${loginUrl}fr">French</a></li
										><li><a href="${loginUrl}ru">Russian</a></li
										><li><a href="${loginUrl}nl">Nederlands</a></li
										><li><a href="${loginUrl}sv">Svenskt</a></li
										><li><a href="${loginUrl}it">Italiano</a></li
										><li><a href="${loginUrl}ur">Urdu</a></li
										><li><a href="${loginUrl}zh_CN">Chinese (Simplified)</a></li
										><li><a href="${loginUrl}de">Deutsch</a></li
										><li><a href="${loginUrl}ja">Japanese</a></li
										><li><a href="${loginUrl}hr">Croatian</a></li
										><li><a href="${loginUrl}cs">Czech</a></li
										><li><a href="${loginUrl}sl">Slovenian</a></li
			                            ><li><a href="${loginUrl}ca">Catalan</a></li
			                            ><li><a href="${loginUrl}mk">Macedonian</a></li
										><li class="last"><a href="${loginUrl}pl">Polish</a></li
									></ul>
			                     </c:otherwise>
			                   </c:choose>
			                       <p>Copyright &copy; 2005 - 2010 . All rights reserved.</p>
                      			  <p>Powered by <a href="#">SSO</a></p>
			                </div>
			            </div>
                    
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/js/cas.js" />"></script>
    </body>
</html>

