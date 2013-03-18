class CasController < ApplicationController

  def validate
    # This controller is required because of a CAS limitation :
    # POST requests are not allowed when redirecting to application.
    # For this reason it's not possible to use /sessions/login
    begin
      login = nil
      assertion = servlet_request.getAttribute(org.jasig.cas.client.util.AbstractCasFilter::CONST_CAS_ASSERTION)
      unless assertion.nil? && assertion.getPrincipal().nil?
        login = assertion.getPrincipal().getName()
      end
      self.current_user = User.authenticate(login, nil, servlet_request)

    rescue Exception => e
      self.current_user = nil
      puts e
    end
    redirect_back_or_default(home_url)
  end

end
