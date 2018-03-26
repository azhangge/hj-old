<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
<title>Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<form th:action="@{/th/bb.do}" th:object="${user}" th:method="post">
		<!-- 
		<input type="text" th:field="*{name}" />
		<input type="text" th:field="${user.tel}" />
		<div th:text="${user.name}"></div>
		<div th:text="${user.tel}"></div>
		<table>
			<tbody th:remove="all-but-first">
				<tr th:each="product:${list}">
					<td th:text="${productStat.count}">1</td>
					<td th:text="${product.name}">Red Chair</td>
					<td th:text="${'$' + #numbers.formatDecimal(product.price, 1, 2)}">$123</td>
					<td th:text="${#dates.format(product.availableBegain, 'yyyy-MM-dd')}">2014-12-01</td>
				</tr>
			</tbody>
		</table>
		<input th:action="@{/th/bb.do?index=1}" type="submit" value="111" />
		<input th:action="@{/th/bb.do?index=2}" type="submit" value="222" />
		<input type="submit" />
		 -->
		<link rel="Stylesheet" type="text/css" href="/JT/css/bootstrap.min.css" />
		<link rel="Stylesheet" type="text/css" href="/JT/css/test.css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/test.js"></script>
		<table class="table table-hover" style="border-collapse: 0" width="50%" id="testTable">
			<thead>
				<tr>
					<th>序号</th>
					<th>用户名</th>
					<th>积分</th>
					<th>开始日期</th>
				</tr>
			</thead>
			<tr th:each="product:${users}">
				<td th:text="${productStat.count+(pageNum-1)*10}">1</td>
				<td th:text="${product.name}">Red Chair</td>
				<td th:text="${'$' + #numbers.formatDecimal(product.price, 1, 2)}">$123</td>
				<td th:text="${#dates.format(product.availableBegain, 'yyyy-MM-dd')}">2014-12-01</td>
			</tr>
		</table>
		<script th:inline="javascript">
		</script>
		<nav>
			<ul class="pagination">
				<li>
					<a th:href="@{${'/th/user.do'}(pageNum=1,size=${pageSize})}">&laquo;</a>
				</li>

				<li>
					<a th:if="${not isFirstPage}" th:href="@{${'/th/user.do'}(pageNum=${pageNum-1},pageSize=${pageSize})}">Previous</a>
					<a th:if="${isFirstPage}" href="javascript:void(0);">Previous</a>
				</li>

				<li th:each="pageNo : ${#numbers.sequence(1, totalPages)}">
					<a th:if="${pageNum eq pageNo}" href="javascript:void(0);">
						<span th:text="${pageNo}" class="active"></span>
					</a>

					<a th:if="${not (pageNum eq pageNo)}" th:href="@{${'/th/user.do'}(pageNum=${pageNo},size=${pageSize})}">
						<span th:text="${pageNo}"></span>
					</a>
				</li>
				<li>
					<a th:if="${not isLastPage}" th:href="@{${'/th/user.do'}(pageNum=${pageNum+1},size=${pageSize})}">Next</a>
					<a th:if="${isLastPage}" href="javascript:void(0);">Next</a>
				</li>

				<li>
					<a th:href="@{${'/th/user.do'}(pageNum=${totalPages},size=${pageSize})}">&raquo;</a>
				</li>
				<li>
					<button type="button" onclick="test2()">上一页</button>
					<button type="button" onclick="test()">下一页</button>
					<button type="button" onclick="test3()">跳转到第</button><input class="input" style="width:20px;" />页
				</li>
			</ul>
		</nav>
	</form>

</body>
</html>