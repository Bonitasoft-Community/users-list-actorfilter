package org.bonitasoft.actorfilter

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.bonitasoft.engine.filter.AbstractUserFilter;
import org.bonitasoft.engine.filter.UserFilterException;
import org.bonitasoft.engine.identity.User;

import groovy.util.logging.Slf4j

@Slf4j
class UsersListFilter extends AbstractUserFilter {
    
    def static final USERS_INPUT = "users"
    def static final AUTOASSIGN_INPUT = "autoassign"
    
    /**
     * Perform validation on the inputs defined on the actorfilter definition (src/main/resources/users-list-actorfilter.def)
     * You should:
     * - validate that mandatory inputs are presents
     * - validate that the content of the inputs is coherent with your use case (e.g: validate that a date is / isn't in the past ...)
     */
    @Override
    def void validateInputParameters() throws ConnectorValidationException {
        checkListOfLongInput(USERS_INPUT)
        checkBoolean(AUTOASSIGN_INPUT)
    }

    def void checkListOfLongInput(String inputName) throws ConnectorValidationException {
        def value = getInputParameter(inputName)
        if (!(value instanceof List)) {
            throw new ConnectorValidationException("'$inputName' mandatory parameter must be List of java.lang.Long")
        }
        if(value.isEmpty()) {
            throw new ConnectorValidationException("'$inputName' mandatory parameter must have at least one element")
        }
        def invalidValues = value.findAll { !(it instanceof Long) }
        if(invalidValues) {
            throw new ConnectorValidationException("'$inputName' mandatory parameter contains elements that are not Long values : ${invalidValues}")
        }
    }
    
    def void checkBoolean(String inputName) throws ConnectorValidationException {
        def value = getInputParameter(inputName)
        if (!(value instanceof Boolean)) {
            throw new ConnectorValidationException("'$inputName' parameter must be a Boolean")
        }
    }
    
    /**
     * @return a list of {@link User} id that are the candidates to execute the task where this filter is defined.
     */
    @Override
    def List<Long> filter(String actorName) throws UserFilterException {
        def selectedUsers = getInputParameter(USERS_INPUT)
        def apiAccessor = getAPIAccessor()
        def processAPI = apiAccessor.getProcessAPI()
        def users = processAPI.getUserIdsForActor(getExecutionContext().getProcessDefinitionId(), actorName, 0, Integer.MAX_VALUE);

        log.info "${USERS_INPUT} input = ${selectedUsers}"
        def usersNotBelongingToActor = selectedUsers.findAll { !users.contains(it) }
        if(usersNotBelongingToActor) {
            log.warn("Following users has been selected to execute the task ${getExecutionContext().getActivityInstanceId()} but do not belong to '$actorName' actor: $usersNotBelongingToActor")
        }
        users.intersect(selectedUsers)
    }

    @Override
    public boolean shouldAutoAssignTaskIfSingleResult() {
        return getInputParameter(AUTOASSIGN_INPUT);
    }
   
}