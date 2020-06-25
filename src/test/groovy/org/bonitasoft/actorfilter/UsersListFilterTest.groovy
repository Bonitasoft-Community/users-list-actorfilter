/**
 * Copyright (C) 2020 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.actorfilter

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.connector.ConnectorValidationException
import org.bonitasoft.engine.connector.EngineExecutionContext

import spock.lang.Specification


class UsersListFilterTest extends Specification {

    def UsersListFilter filter
    APIAccessor apiAccessor = Mock(APIAccessor)
    ProcessAPI processApi = Mock(ProcessAPI)
    EngineExecutionContext engineExecutionContext = Mock(EngineExecutionContext)

    def setup() {
        apiAccessor.getProcessAPI() >> processApi
        filter = new UsersListFilter()
        filter.setAPIAccessor(apiAccessor)
        filter.setExecutionContext(engineExecutionContext)
    }

    def should_throw_exception_if_mandatory_input_is_missing() {
        given: 'An actorfilter without input'
        filter.setInputParameters([:])

        when: 'Validating inputs'
        filter.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }

    def should_throw_exception_if_mandatory_input_is_not_a_list() {
        given: 'users input with a negative input'
        filter.setInputParameters([(UsersListFilter.USERS_INPUT):-1])

        when: 'Validating inputs'
        filter.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }
    
    def should_throw_exception_if_mandatory_input_is_empty() {
        given: 'users input with an empty list'
        filter.setInputParameters([(UsersListFilter.USERS_INPUT):[]])

        when: 'Validating inputs'
        filter.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }
    
    def should_throw_exception_if_mandatory_input_is_not_a_list_of_long() {
        given: 'users input with a list of string'
        filter.setInputParameters([(UsersListFilter.USERS_INPUT):['romain','adrien']])

        when: 'Validating inputs'
        filter.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }

    def should_throw_exception_if_autoassign_input_is_not_a_boolean() {
        given:'autoassign input with a string'
        filter.setInputParameters([(UsersListFilter.AUTOASSIGN_INPUT):'1'])

        when: 'Validating inputs'
        filter.validateInputParameters()

        then: 'ConnectorValidationException is thrown'
        thrown ConnectorValidationException
    }
    
    def should_enable_autoassign() {
        given:'Autoassign parameter set to true'
        filter.setInputParameters([(UsersListFilter.AUTOASSIGN_INPUT):true])

        when:
        def autoassign = filter.shouldAutoAssignTaskIfSingleResult()
        
         then: 'Should autoassign task if single result'
        autoassign == true
    }
    
    def should_disable_autoassign() {
        given: 'Autoassign parameter set to false'
        filter.setInputParameters([(UsersListFilter.AUTOASSIGN_INPUT):false])

        when:
        def autoassign = filter.shouldAutoAssignTaskIfSingleResult()
        
        then: 'Should not autoassign task if single result'
        autoassign == false
    }

    def should_return_the_list_of_selected_candidates() {
        given: 'The list of users for MyActor'
        processApi.getUserIdsForActor(_ as Long, 'MyActor', 0, Integer.MAX_VALUE) >> [1L, 2L, 3L]

        and: 'The list of selected users'
        filter.setInputParameters([(UsersListFilter.USERS_INPUT): [2L, 3L]])

        when: 'Applying filter to the existing users'
        def candidates = filter.filter("MyActor");

        then: 'Only selected users are candidates'
        assert candidates.contains(2L)
        assert candidates.contains(3L)
        assert !candidates.contains(1L)
    }
    
    def should_not_return_unknown_selected_users() {
        given: 'The list of users for MyActor'
        processApi.getUserIdsForActor(_ as Long, 'MyActor', 0, Integer.MAX_VALUE) >> [1L, 2L, 3L]

        and: 'The list of selected users'
        filter.setInputParameters([(UsersListFilter.USERS_INPUT): [5L, 6L]])

        when: 'Applying filter to the existing users'
        def candidates = filter.filter("MyActor");

        then: 'Only selected users are candidates'
        assert candidates.isEmpty()
    }
}