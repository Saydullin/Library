<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>

<jsp:useBean id="books" scope="request" type="java.util.List"/>
<jsp:useBean id="numberOfPages" scope="request" type="java.lang.Long"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<tags:master pageTitle="Book List">
  <fmt:setLocale value="${sessionScope.lang}"/>
  <fmt:setBundle basename="messages"/>

  <c:choose>
    <c:when test="${not empty inputErrors}">
      <div class="container">
        <div class="panel panel-danger">
          <div class="panel-heading"><fmt:message key="error_title" /></div>
          <div class="panel-body">
            <fmt:message key="error_updating_cart" />
          </div>
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <c:if test="${not empty successMessage}">
        <div class="container">
          <div class="panel panel-success">
            <div class="panel-heading"><fmt:message key="success_title" /></div>
            <div class="panel-body">${successMessage}</div>
          </div>
        </div>
      </c:if>
    </c:otherwise>
  </c:choose>

  <div class="row">
    <div class="container">
      <div class="container">
        <form class="float-right">
          <input name="query" value="${param.query}">
          <input type="hidden" name="command" value="Product_List">
          <button class="btn btn-light"><fmt:message key="button_search" /></button>
        </form>
      </div>
    </div>
  </div>
  <div class="panel"></div>
  <div class="row">
    <div class="col-2"></div>
    <div class="col-8">
      <table class="table table-hover table-bordered text-center">
        <thead>
        <tr class="bg-light">
          <td><fmt:message key="book_image" /></td>
          <td>
            <fmt:message key="book_name" />
            <tags:sortLink sort="bookName" order="asc"/><tags:sortLink sort="bookName" order="desc"/>
          </td>
          <td>
            <fmt:message key="book_author" />
            <tags:sortLink sort="author" order="asc"/><tags:sortLink sort="author" order="desc"/>
          </td>
          <td><fmt:message key="book_genres" /></td>
          <td>
            <fmt:message key="book_publishing" />
            <tags:sortLink sort="releaseYear" order="asc"/>
            <tags:sortLink sort="releaseYear" order="desc"/>
          </td>
          <td>
            <fmt:message key="book_price" />
            <tags:sortLink sort="price" order="asc"/>
            <tags:sortLink sort="price" order="desc"/>
          </td>
          <td><fmt:message key="table_action" /></td>
        </tr>
        </thead>
        <c:forEach var="book" items="${books}">
          <tr>
            <td class="align-middle">
              <img class="rounded" src="/images/${book.imageUrl}">
            </td>
            <td class="align-middle">
              <form id="productFormBrand" action="/" method="GET">
                <input type="hidden" name="command" value="product_details">
                <input type="hidden" name="phone_id" value="${book.id}">
                <button type="submit">
                    ${book.bookName}
                </button>
              </form>
            </td>
            <td class="align-middle">
              <form id="productFormModel" action="/" method="GET">
                <input type="hidden" name="command" value="product_details">
                <input type="hidden" name="phone_id" value="${book.id}">
                <button type="submit">
                    ${book.author}
                </button>
              </form>
            </td>
            <td class="align-middle">
              <ul>
                <c:forEach var="genre" items="${book.genres}">
                  <li>${genre.code}</li>
                </c:forEach>
              </ul>
            </td>
            <td class="align-middle">${book.releaseYear}"</td>
            <td class="align-middle">$ ${book.price}</td>
            <td class="align-middle">
                      <c:choose>
                        <c:when test="${not empty sessionScope.login}">
                            <form action="/" method="post">
                              <input type="hidden" name="command" value="cart_add">
                        </c:when>
                        <c:otherwise>
                            <form action="/" method="get">
                              <input type="hidden" name="command" value="authorisation">
                        </c:otherwise>
                      </c:choose>
                <input type="hidden" name="id" value="${book.id}">
                <input type="hidden" name="page_type" value="productList">
                <input type="number" name="quantity" id="quantity${book.id}" min="1" required>
                <button class="btn btn-lg btn-outline-light text-dark border-dark float-right" type="submit" style="font-size: 14px"><fmt:message key="button_add" /></button>
              </form>
                              <c:if test="${not empty inputErrors.get(book.id)}">
                                  <div class="error" style="genre: red">${inputErrors[book.id]}</div>
                              </c:if>
            </td>
          </tr>
        </c:forEach>
      </table>
      <tags:pages page="${empty param.page or param.page lt 1 ? 1 : param.page}" lastPage="${numberOfPages}"/>
    </div>
  </div>
</tags:master>