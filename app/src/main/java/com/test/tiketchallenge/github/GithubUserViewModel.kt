package com.test.tiketchallenge.github

import com.test.tiketchallenge.base.BaseViewModel
import com.test.tiketchallenge.network.ApiService
import javax.inject.Inject

class GithubUserViewModel @Inject constructor(apiService : ApiService) : BaseViewModel<GithubUserContract>(apiService){

}
