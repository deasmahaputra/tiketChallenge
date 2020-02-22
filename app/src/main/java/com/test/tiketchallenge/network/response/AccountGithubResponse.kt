package com.test.tiketchallenge.network.response

data class AccountGithubResponse(
	val totalCount: Int? = null,
	val incompleteResults: Boolean? = null,
	val items: List<ItemsItem?>? = null
)
