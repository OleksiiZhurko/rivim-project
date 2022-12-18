import React, {Component} from 'react'
import StorageService from '../../service/storageService'
import AuthService from '../../service/authService'
import {Link} from 'react-router-dom'
import {ToastContainer} from 'react-toastify'
import ToastTemplate from '../../util/toastTemplate'

const loginFormStyle = {
    width: '25%',
    margin: '10% auto',
    fontSize: '15px'
}

const btnStyle = {
    fontWeight: 'bold',
    minHeight: '38px',
    borderRadius: '2px'
}

class LoginPage extends Component {

    constructor(props) {
        super(props)

        if (StorageService.isAuthenticated()) {
            this.props.history.push('/redirect')
        }

        this.state = {
            username: '',
            password: ''
        }
    }

    handleSubmit = event => {
        event.preventDefault()

        AuthService.login(this.loginForm)
            .then(() => {
                this.props.history.push('/redirect')
            }).catch(err => {
                if (err.response?.data?.reasons) {
                    ToastTemplate.warn(err.response.data.reasons)
                } else {
                    ToastTemplate.error('Internal server error')
                }
            })
    }

    handleChange = event => {
        this.setState({[event.target.name]: event.target.value})
    }

    get loginForm() {
        return {
            username: this.state.username,
            password: this.state.password
        }
    }

    render() {
        return (
            <div>
                <ToastContainer />
                <div style={loginFormStyle}>
                    <h2 className='text-center my-3'>Sign in</h2>
                    <form>
                        {(this.state.loginFailed) && <p className='error'>Invalid username or password</p>}
                        <div className='form-group'>
                            <input className='form-control' name='username'
                                   placeholder='Username'
                                   type='text' value={this.state.username} onChange={this.handleChange} />
                        </div>
                        <div className='form-group'>
                            <input className='form-control'
                                   name='password'
                                   placeholder='Password'
                                   type='password' value={this.state.password} onChange={this.handleChange} />
                        </div>
                        <div className='form-group'>
                            <button type='submit' style={btnStyle} onClick={this.handleSubmit} className='btn btn-dark btn-block'>
                                Log in
                            </button>
                        </div>
                        <div className='col-12 mt-2 text-right p-0'>
                            <Link to={'/register'} className='link'>Create an account</Link>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}

export default LoginPage
