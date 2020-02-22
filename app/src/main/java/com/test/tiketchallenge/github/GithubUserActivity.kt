package com.test.tiketchallenge.github

import androidx.fragment.app.Fragment
import com.test.tiketchallenge.base.BaseActivity
import com.test.tiketchallenge.databinding.ActivityGithubBinding
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class GithubUserActivity : BaseActivity<ActivityGithubBinding, GithubUserViewModel>(), GithubUserContract, HasSupportFragmentInjector {


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}