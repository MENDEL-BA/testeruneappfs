import * as cypress from "cypress";
import { login } from "../support/login";

describe('Deconnection test', ()=>{
    it('Test deconnection',()=>{
        cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            []
        ).as('session');
        login('userRoleAdmin', 'user@yopmail.com', 'passer@123');
        cy.get('span').contains('Logout').click();
        cy.get('span').contains('Login');
    });
})