import * as cypress from "cypress";
import {login} from "../support/login";

describe('User info test', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      { fixture: 'sessionsData' }
    ).as('sessionsData');
  });

  it('Affichage info user', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      { fixture: 'userRoleUser' }
    ).as('information');
    login('userRoleUser', 'user@yopmail.com', 'user');
    cy.get('span').contains('Account').click();
    cy.contains('button', 'Detail').should('exist');
    cy.fixture('userRoleUser').then((json) => {
      const name = json.firstName + ' ' + json.lastName.toUpperCase();
      cy.contains('p', name);
      cy.contains('p', json.email);
    });
  });
})
