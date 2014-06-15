<% if (context.authenticated) { %>
	${ ui.format(context.authenticatedUser.person) }
	<small>
		(${ context.authenticatedUser.username ?: context.authenticatedUser.systemId })
	</small>
<% } %>