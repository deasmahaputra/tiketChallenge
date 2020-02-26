package com.test.tiketchallenge.github

import android.annotation.SuppressLint
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
import com.test.tiketchallenge.github.adapter.PaginationScrollListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_github.*
import java.util.concurrent.TimeUnit
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

        viewModel.fetchGithubAccount()

        initViews()
    }

    private fun initViews() {
        binding.accounntNameEt.afterTextChanged {
            viewModel.onInputStateChanged(it, defPage)
            adapter.clearData()
            defPage = 0
        }

        viewModel.githubAccount.observe(this, Observer { githubAccount ->
            val githubAccountData = githubAccount.items
            if(githubAccountData?.isNotEmpty() == true){
                setAccountAvailable(true)
                adapter.setDataAccount(githubAccountData)
            }else{
                setAccountAvailable(false)
            }
        })

        viewModel.isGithubAccountSearching.observe(this, Observer {
            if(it == true){
                binding.progressBar.visibility = View.VISIBLE
                binding.textNotFound.visibility = View.GONE
                binding.layoutFailure.visibility = View.GONE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.progressBarScroll.visibility = View.GONE
            }
        })

        binding.githubRv.addOnScrollListener(scrollData(defPage))


    }

    override fun errorConnection() {
        binding.layoutSuccess.visibility = View.GONE
        binding.layoutFailure.visibility = View.VISIBLE
        binding.textNotFound.visibility = View.GONE
    }

    private fun scrollData(page: Int): PaginationScrollListener {
        return object : PaginationScrollListener() {
            @SuppressLint("CheckResult")
            override fun onLoadMore() {
                binding.progressBarScroll.visibility = View.VISIBLE
                Completable
                    .timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        defPage += 1
                        viewModel.onInputStateChanged(accounnt_name_et.text.toString(), defPage)
                    }, {
                        binding.textNotFound.visibility = View.GONE
                    })
            }
        }
    }

    private fun setAccountAvailable(it : Boolean){
        if(it){
            binding.layoutSuccess.visibility = View.VISIBLE
            binding.textNotFound.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.progressBarScroll.visibility = View.GONE
            binding.layoutFailure.visibility = View.GONE
        }else{
            if(binding.accounntNameEt.text?.isEmpty() == true){
                binding.textNotFound.visibility = View.GONE
            }else{
                binding.textNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}