package com.test.tiketchallenge.github

import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.test.tiketchallenge.R
import com.test.tiketchallenge.base.BaseActivity
import com.test.tiketchallenge.databinding.ActivityGithubBinding
import com.test.tiketchallenge.extension.afterTextChanged
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_github.*
import javax.inject.Inject

class GithubUserActivity : BaseActivity<ActivityGithubBinding, GithubUserViewModel>(), GithubUserContract, HasSupportFragmentInjector {


    @Inject
    internal lateinit var factoryModel: ViewModelProvider.Factory

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var viewModel: GithubUserViewModel
    private lateinit var binding : ActivityGithubBinding


    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_github

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun getViewModel(): GithubUserViewModel{
        viewModel = ViewModelProviders.of(this, factoryModel).get(GithubUserViewModel::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        viewModel.setNavigator(this)

        viewModel.fetchGithubAccount()

        viewModel.getDeas()



        initViews()
    }

    private fun initViews() {

        accounnt_name_et.afterTextChanged {
            handleInputQuery(it)
        }

    }

    private fun handleInputQuery(it: String) {
        viewModel.onInputStateChanged(it)
    }
}