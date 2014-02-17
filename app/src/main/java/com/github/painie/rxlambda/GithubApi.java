package com.github.painie.rxlambda;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GithubApi {

    @GET("/orgs/{company}/repos")
    Observable<List<Repo>> getRepositories(@Path("company") String company);
}
