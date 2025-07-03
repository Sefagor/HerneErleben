# Use official Node image with Alpine for smaller size
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package manifests first to leverage Docker cache
COPY package*.json ./

# Install dependencies, including react-calendar
RUN npm install

# Copy the rest of the application
COPY . .

# Expose the port your app runs on
EXPOSE 3000

# Run the app in development mode
CMD ["npm", "start"]