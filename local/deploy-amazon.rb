#!/usr/bin/ruby

require "aws-sdk"
require 'pp'

# -- BEGIN figure out name suffix
@time = Time.new
@date_suffix = @time.strftime("%Y-%m-%d-%H:%M")
@file_name = "#{ARGV[1]}-#{@date_suffix}"
# -- END figure out name suffix

# Get a global beanstalk client
@beanstalk = AWS::ElasticBeanstalk.new(
    :access_key_id => 'AKIAJZ3IPCTVEDQZAKAA',
    :secret_access_key => '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',
    :elastic_beanstalk_endpoint => 'elasticbeanstalk.eu-west-1.amazonaws.com').client

# Registers and updates beanstalk versions
def beanstalk
  puts 'Registering new version...'
  @beanstalk.create_application_version(:application_name => 'eHub Development', :version_label => @file_name, :source_bundle => {:s3_bucket => 'test-wars', :s3_key => @file_name})
  puts '  done!'
  puts 'Using new version...'
  @beanstalk.update_environment(:environment_name => 'ehub-dev',:environment_id => 'e-77jwz9hwyi', :version_label => @file_name)

end

#rids old versions from beanstalk
def clean_beanstalk
  puts 'Cleaning other versions from Beanstalk and s3...'
  versions = @beanstalk.describe_application_versions(:application_name => 'eHub Development')[:application_versions]
  versions.each do |version|
    unless version[:version_label] == @file_name
      @beanstalk.delete_application_version(:application_name => 'eHub Development', :version_label => version[:version_label], :delete_source_bundle => true)
    end
  end
  puts "  done!"
end

# read file that was specified as the first parameter (should be location of war-file to deploy)
def get_data()
  puts 'reading data to send...'
  data = open(ARGV[0], "rb") do |io|
    io.read

  end
  puts '  data read - sending!'
  data
end

# upload war-file to s3 so we can register it with beanstalk
def s3
  puts 'getting s3 client...'
  s3 = AWS::S3.new(
      :access_key_id => 'AKIAJZ3IPCTVEDQZAKAA',
      :secret_access_key => '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',
      :s3_endpoint => 's3-eu-west-1.amazonaws.com').client
  puts '  got s3 client!'
  puts 'sending file to S3...'
  s3.put_object({
                    :bucket_name => 'test-wars',
                    :key => @file_name,
                    :data => get_data
                })
  puts '  file sent!'

end

#1. Upload war-file to S3,
s3
#2. Register new version with beanstalk, then switch to that version
beanstalk
#3. Delete all old versions and their war-files
clean_beanstalk

