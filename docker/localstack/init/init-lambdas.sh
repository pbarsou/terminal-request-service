#!/bin/bash
echo "Initializing Lambdas in LocalStack (Direct Mode)..."

create_lambda() {
    local name=$1
    local handler=$2
    local jar=$3

    echo "Creating lambda: $name"
    awslocal lambda create-function \
        --function-name "$name" \
        --runtime java21 \
        --handler "$handler" \
        --zip-file "fileb:///tmp/lambdas/$jar" \
        --role arn:aws:iam::000000000000:role/lambda-role
}

create_lambda "customer-validation" "com.desafio.lamda.CustomerValidationHandler::handleRequest" "LamdaCustomerValidation-1.0-SNAPSHOT.jar"
create_lambda "terminal-reservation" "com.desafio.lamda.POSTerminalReservationHandler::handleRequest" "LambdaTerminalReservation-1.0-SNAPSHOT.jar"
create_lambda "terminal-reservation-compensator" "com.desafio.lamda.TerminalReservationCompensatorHandler::handleRequest" "LambdaDeliverySchedulingCompensation-1.0-SNAPSHOT.jar"
create_lambda "delivery-scheduling" "com.desafio.lamda.DeliverySchedulingHandler::handleRequest" "LambdaDeliveryService-1.0-SNAPSHOT.jar"

echo "Lambdas initialized successfully in Direct Mode!"
