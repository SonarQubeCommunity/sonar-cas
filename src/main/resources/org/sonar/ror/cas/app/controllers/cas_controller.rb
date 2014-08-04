class CasController < ApplicationController

  skip_before_filter :check_authentication

  def validate
    # This controller is required because of a CAS limitation :
    # POST requests are not allowed when redirecting to application.
    # For this reason it's not possible to use /sessions/login
    begin
      login = nil
      assertion = servlet_request.getAttribute("_const_cas_assertion_")
      unless assertion.nil? || assertion.getPrincipal().nil?
        login = assertion.getPrincipal().getName()
      end
      self.current_user = User.authenticate(login, nil, servlet_request)

    rescue Exception => e
      self.current_user = nil
      logger.error(e)
    end
    redirect_back_or_default(home_url)
  end

end
