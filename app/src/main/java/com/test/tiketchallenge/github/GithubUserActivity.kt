package com.test.tiketchallenge.github

import com.test.tiketchallenge.github.adapter.PaginationScrollListener
import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.tiketchallenge.R
import com.test.tiketchallenge.base.BaseActivity
import com.test.tiketchallenge.databinding.ActivityGithubBinding
import com.test.tiketchallenge.extension.afterTextChanged
import com.test.tiketchallenge.github.adapter.GithubUserAdapter
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

    private lateinit var adapter : GithubUserAdapter

    private lateinit var layoutManager  : LinearLayoutManager

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false

    private var defPage = 0


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

        layoutManager = LinearLayoutManager(this)
        github_rv.layoutManager = layoutManager
        adapter = GithubUserAdapter(this)
        github_rv.adapter = adapter


        initViews()
    }

    private fun initViews() {
        layoutFailure.visibility = View.GONE
        progressBar.visibility = View.GONE


        accounnt_name_et.afterTextChanged {
            handleInputQuery(it, 0)
            adapter.clearData()
        }

        viewModel.githubAccount.observe(this, Observer { githubAccount ->
            val githubAccountData = githubAccount.items
            if(githubAccountData?.isNotEmpty() == true){
                isLoading = false
                progressBar.visibility = View.GONE
                layoutSuccess.visibility = View.VISIBLE
                layoutFailure.visibility = View.GONE
                adapter.setDataAccount(githubAccountData)
            }
        })

        viewModel.isGithubAccountSearching.observe(this, Observer {
            if(it == true){
                progressBar.visibility = View.VISIBLE
            }else{
                progressBar.visibility = View.GONE
            }
        })

//        github_rv.addOnScrollListener(object : com.test.tiketchallenge.github.adapter.PaginationScrollListener(layoutManager){
//            override fun isLastPage(): Boolean {
//                return isLastPage
//            }
//
//            override fun isLoading(): Boolean {
//                return isLoading
//            }
//
//            override fun loadMoreItems() {
//                isLoading = true
//                progressBar.visibility = View.VISIBLE
//                defPage += 1
//                viewModel.onInputStateChanged(accounnt_name_et.text.toString(), defPage)
//            }
//
//        })

        defPage += 1
        github_rv.addOnScrollListener(scrollData(defPage))

    }

    private fun handleInputQuery(it: String, page : Int) {
        viewModel.onInputStateChanged(it, page)
    }

    override fun errorConnection() {
        layoutSuccess.visibility = View.GONE
        layoutFailure.visibility = View.VISIBLE
    }

    private fun scrollData(page: Int): PaginationScrollListener {
        return object : PaginationScrollListener() {
            override fun onLoadMore() {
                viewModel.onInputStateChanged(accounnt_name_et.text.toString(), page)
            }
        }
    }
}