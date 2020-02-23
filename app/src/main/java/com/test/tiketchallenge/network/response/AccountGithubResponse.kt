package com.test.tiketchallenge.network.response

data class AccountGithubResponse(
	var total_count: Int? = null,
	var incomplete_results: Boolean? = null,
	var items: List<ItemsItem?>? = null
)
