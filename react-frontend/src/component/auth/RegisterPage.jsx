import React, {Component} from 'react'
import {Formik} from 'formik'
import StorageService from '../../service/storageService'
import {Link} from 'react-router-dom'
import AuthService from '../../service/authService'
import Validator from '../../util/validator'
import FormikUtils from '../../util/formikUtils'
import {ToastContainer} from 'react-toastify'
import ToastTemplate from '../../util/toastTemplate'

class RegisterPage extends Component {

    constructor(props) {
        super(props)

        if (StorageService.isAuthenticated()) {
            this.props.history.push('/redirect')
        }

        this.state = {
            captcha: ''
        }

        this.setCaptcha()
    }

    setCaptcha() {
        AuthService.getCaptcha()
            .then(result => {
                this.setState({
                    captchaImg: result.data.captcha,
                    code: result.data.code
                })
            }).catch(() => {
                ToastTemplate.error('Internal server error')
            })
    }

    handleCaptcha = event => {
        this.setState({captcha: event.target.value})
    }

    validateCaptcha() {
        return this.state.code === this.state.captcha
    }

    handleSubmit = (values) => {
        if (this.validateCaptcha(values.captcha)) {
            AuthService.register(FormikUtils.formUser(values))
                .then(() => {
                    this.props.history.push('/login')
                }).catch(err => {
                    if (err.response?.data?.error) {
                        if (err.response.data.error === 'UsernameNotUniqueException') {
                            ToastTemplate.warn('Username already exists')
                        } else if (err.response.data.error === 'EmailNotUniqueException') {
                            ToastTemplate.warn('Email already exists')
                        } else {
                            ToastTemplate.error('Internal server error')
                        }
                    } else {
                        ToastTemplate.error('Internal server error')
                    }
                })
        } else {
            this.setCaptcha()
        }
    }

    render() {
        return (
            <div>
                <ToastContainer />
                <div className='container mt-3'>
                    <h2>Registration</h2>
                    <Formik
                        initialValues={FormikUtils.initUser()}
                        validate={Validator.validateCreate}
                        validationSchema={this.validationSchema}
                        onSubmit={this.handleSubmit}>
                        {({
                              errors,
                              touched,
                              values,
                              handleBlur,
                              handleChange,
                              handleSubmit
                          }) => (
                            <form onSubmit={handleSubmit}>
                                {errors.non_field_errors && <div>{errors.non_field_errors}</div>}
                                <div className='form-group required row'>
                                    <label htmlFor='inputUsername' className='col-sm-2 col-form-label'>
                                        Username
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='text'
                                               className={'form-control' + (touched.username && errors.username ? ' is-invalid' : '')}
                                               name='username'
                                               id='inputUsername'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.username}/>
                                        {touched.username && errors.username && <div className={'error'}>{errors.username}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label htmlFor='inputPassword' className='col-sm-2 col-form-label'>
                                        Password
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='password'
                                               className={'form-control' + (touched.password && errors.password ? ' is-invalid' : '')}
                                               name='password'
                                               id='inputPassword'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.password}/>
                                        {touched.password && errors.password && <div className={'error'}>{errors.password}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label htmlFor='inputPasswordAgain' className='col-sm-2 col-form-label'>
                                        Password confirm
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='password'
                                               className={'form-control' + (touched.rePassword && errors.rePassword ? ' is-invalid' : '')}
                                               name='rePassword'
                                               id='inputPasswordAgain'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.rePassword}/>
                                        {touched.rePassword && errors.rePassword && <div className={'error'}>{errors.rePassword}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label htmlFor='inputEmail' className='col-sm-2 col-form-label'>
                                        Email
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='email'
                                               className={'form-control' + (touched.email && errors.email ? ' is-invalid' : '')}
                                               name='email'
                                               id='inputEmail'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.email}/>
                                        {touched.email && errors.email && <div className={'error'}>{errors.email}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label htmlFor='inputFirstName' className='col-sm-2 col-form-label'>
                                        First name
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='text'
                                               className={'form-control' + (touched.firstName && errors.firstName ? ' is-invalid' : '')}
                                               name='firstName'
                                               id='inputFirstName'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.firstName}/>
                                        {touched.firstName && errors.firstName && <div className={'error'}>{errors.firstName}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label htmlFor='inputLastName' className='col-sm-2 col-form-label'>
                                        Last name
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='text'
                                               className={'form-control' + (touched.lastName && errors.lastName ? ' is-invalid' : '')}
                                               name='lastName'
                                               id='inputLastName'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.lastName}/>
                                        {touched.lastName && errors.lastName && <div className={'error'}>{errors.lastName}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label htmlFor='inputBirthday' className='col-sm-2 col-form-label'>
                                        Birthday
                                    </label>
                                    <div className='col-sm-10'>
                                        <input type='date'
                                               className={'form-control' + (touched.birthday && errors.birthday ? ' is-invalid' : '')}
                                               name='birthday'
                                               id='inputBirthday'
                                               onChange={handleChange}
                                               onBlur={handleBlur}
                                               value={values.birthday}/>
                                        {touched.birthday && errors.birthday && <div className={'error'}>{errors.birthday}</div>}
                                    </div>
                                </div>
                                <div className='form-group required row'>
                                    <label className='col-sm-2 col-form-label' htmlFor='inputRole'>
                                        Role
                                    </label>
                                    <div className='col-sm-10' defaultValue={'user'}>
                                        <select className='custom-select' id='inputRole'>
                                            <option value='user'>User</option>
                                        </select>
                                        <div className='invalid-feedback'>Provide role</div>
                                    </div>
                                </div>
                                <div className='form-group row'>
                                    <img className='col-sm-2 col-form-label' src={this.state.captchaImg} alt='captcha' />
                                    <div className='col-sm-10'>
                                        <input type='text'
                                               className='form-control'
                                               name='captchaConfirm'
                                               id='inputCaptcha'
                                               value={this.state.captcha}
                                               onChange={this.handleCaptcha}/>
                                        <div className='invalid-feedback'>Invalid captcha</div>
                                    </div>
                                </div>
                                <div className='form-group row'>
                                    <div className='col-sm-10'>
                                        <button type='submit' className='btn btn-dark mr-3'>Ok</button>
                                        <Link to={'/login'} id='auth' className='btn btn-light'>Cancel</Link>
                                    </div>
                                </div>
                            </form>
                        )}
                    </Formik>
                </div>
            </div>
        )
    }
}

export default RegisterPage
