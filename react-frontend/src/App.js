import './App.css'
import 'react-toastify/dist/ReactToastify.css'
import React, {Component} from 'react'
import {BrowserRouter, Redirect, Route, Switch} from 'react-router-dom'
import LoginPage from './component/auth/LoginPage'
import RegisterPage from './component/auth/RegisterPage'
import Redirection from './component/redirect/Redirection'
import UserPage from './component/user/UserPage'
import AdminPage from './component/admin/AdminPage'
import CreateUpdatePage from './component/admin/CreateUpdatePage'


class App extends Component {
    render() {
        return (
            <BrowserRouter>
                <Switch>
                    <Route exact path='/login' component={LoginPage}/>
                    <Route exact path='/register' component={RegisterPage}/>
                    <Route exact path='/admin' component={AdminPage}/>
                    <Route exact path='/admin/create_user' component={CreateUpdatePage}/>
                    <Route exact path='/admin/update_user/:username' component={CreateUpdatePage}/>
                    <Route exact path='/user' component={UserPage}/>

                    <Route exact path='/redirect' component={Redirection}/>

                    <Redirect to='/redirect' />
                </Switch>
            </BrowserRouter>
        )
    }
}

export default App
