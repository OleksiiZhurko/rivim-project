import * as Yup from 'yup'

class Validator {

    validateCreate = values => {
        try {
            this.getValidationCreateSchema().validateSync(values, {abortEarly: false})
            return {}
        } catch (error) {
            return this.getErrorsFromValidationError(error)
        }
    }

    validateUpdate = values => {
        try {
            this.getValidationUpdateSchema().validateSync(values, {abortEarly: false})
            return {}
        } catch (error) {
            return this.getErrorsFromValidationError(error)
        }
    }

    getValidationCreateSchema = () => {
        return Yup.object().shape({
            username: Yup.string()
                .min(2, 'Username should contain 2 symbols min')
                .max(20, 'Username should contain 20 symbols max')
                .required('Username cannot be empty'),
            password: Yup.string()
                .min(4, 'Password should contain 4 symbols min')
                .max(128, 'Password should contain 128 symbols max')
                .required('Password cannot be empty'),
            rePassword: Yup.string()
                .required('Password confirm cannot be empty')
                .oneOf([Yup.ref('password'), null], 'Passwords are different'),
            email: Yup.string()
                .matches(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/, 'Wrong email format')
                .required('Email cannot be empty'),
            firstName: Yup.string()
                .min(2, 'Firstname should contain 2 symbols min')
                .max(32, 'Firstname should contain 32 symbols max')
                .required('Firstname cannot be empty'),
            lastName: Yup.string()
                .min(2, 'Lastname should contain 2 symbols min')
                .max(32, 'Lastname should contain 32 symbols max')
                .required('Lastname cannot be empty'),
            birthday: Yup.date()
                .nullable()
                .transform((cur, orig) => orig === '' ? null : cur)
                .max(new Date(), 'Birthday cannot be future')
                .required('Birthday cannot be empty'),
        })
    }

    getValidationUpdateSchema = () => {
        return Yup.object().shape({
            password: Yup.string()
                .matches(/^$|^.{4,128}$/, 'Password should contain 4 min and 128 symbols max or be empty'),
            rePassword: Yup.string()
                .oneOf([Yup.ref('password'), null], 'Passwords are different'),
            email: Yup.string()
                .matches(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/, 'Wrong email format')
                .required('Email cannot be empty'),
            firstName: Yup.string()
                .min(2, 'Firstname should contain 2 symbols min')
                .max(32, 'Firstname should contain 32 symbols max')
                .required('Firstname cannot be empty'),
            lastName: Yup.string()
                .min(2, 'Lastname should contain 2 symbols min')
                .max(32, 'Lastname should contain 32 symbols max')
                .required('Lastname cannot be empty'),
            birthday: Yup.date()
                .nullable()
                .transform((cur, orig) => orig === '' ? null : cur)
                .max(new Date(), 'Birthday cannot be future')
                .required('Birthday cannot be empty'),
        })
    }

    getErrorsFromValidationError = validationError => {
        const FIRST_ERROR = 0
        return validationError.inner.reduce((errors, error) => {
            return {
                ...errors,
                [error.path]: error.errors[FIRST_ERROR]
            }
        }, {})
    }
}

export default new Validator()
