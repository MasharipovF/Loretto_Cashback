package com.example.lorettocashback.data.repository

import android.util.Log
import com.example.lorettocashback.core.GeneralConsts
import com.example.lorettocashback.data.Preferences
import com.example.lorettocashback.data.remote.services.LoginService
import com.example.lorettocashback.data.remote.services.UsersService
import com.example.lorettocashback.domain.dto.login.LoginRequestDto
import com.example.lorettocashback.util.ErrorUtils
import com.example.lorettocashback.util.retryIO

interface LoginRepository {
    suspend fun requestLogin(): Any?
    suspend fun getUserDefaults(username: String): Any?
}

class LoginRepositoryImpl(
) : LoginRepository {

    override suspend fun requestLogin(): Any? {

        // THIS IS NEEDED FOR THIS. WHEN WE LOAD FIRST IN ENGLISH AND THEN IN RUSSIAN LANGUAGE, THEN RUSSIAN LANGUAGE ERROR STRINGS DISPLAYED CORRECTLY.
        // THAT IS WHY WE FIRST LOGIN IN ENGLISH, AND THE NEXT TIME LOGIN IN RUSSIAN
        val isFirstLogin = Preferences.firstLogin

        val loginService: LoginService = LoginService.get()

        Log.wtf("PPPPI", GeneralConsts.COMPANY_DB)

        val response = retryIO {
            loginService.requestLogin(
                LoginRequestDto(
                    GeneralConsts.COMPANY_DB,
                    GeneralConsts.PASSWORD,
                    GeneralConsts.LOGIN,
                    if (isFirstLogin) null else 24
                )
            )
        }

        Log.wtf("PPPPI", "$response")


        Log.wtf("PPPPI", "Keldi")

        return if (response.isSuccessful) {
            Preferences.firstLogin = false
            response.body()
        } else {
            return ErrorUtils.errorProcess(response)
        }
    }

    override suspend fun getUserDefaults(
        username: String
    ): Any? {
        val userDefaultsService: UsersService = UsersService.get()

        val userDefResponse = retryIO {
            userDefaultsService.getUserDefaults(userCode = "UserCode eq '$username'")
        }
        return if (userDefResponse.isSuccessful) {
            userDefResponse.body()?.transform()
        } else {
            return ErrorUtils.errorProcess(userDefResponse)
        }
    }

    /*override suspend fun getUserDefaults(
        username: String
    ): UserDefaults? {
        val userDefaultsService: UsersService = UsersService.get()

        val queryPath = "\$crossjoin(Users,UserDefaultGroups)"
        val queryOption =
            "\$expand=Users(\$select=UserCode,UserName,Defaults,Department,U_branch,U_whs,U_inWhs,U_brWhs,U_defCustomer,U_acctUZS,U_acctUSD),UserDefaultGroups(\$select=Code,Name,Warehouse,SalesEmployee)&" +
                    "\$filter=Users/Defaults eq UserDefaultGroups/Code and Users/UserCode eq '$username'"
        val userDefResponse = retryIO {
            userDefaultsService.getUserDefaults(CrossJoin(queryOption, queryPath))
        }
        return if (userDefResponse.isSuccessful) {
            userDefResponse.body()
        } else {
            Log.d(
                "USERDEFAULTS",
                userDefResponse.errorBody()!!.string()
            )
            null
        }
    }*/

}