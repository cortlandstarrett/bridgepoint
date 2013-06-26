#!/bin/bash -u
#=====================================================================
#
# File:      $RCSfile: create_release_functions.sh,v $
# Version:   $Revision: 1.66 $
# Modified:  $Date: 2013/05/10 13:26:45 $
#
#(c) Copyright 2004-2012 by Mentor Graphics Corp. All rights reserved.
#
#=====================================================================
# This document contains information proprietary and confidential to
# Mentor Graphics Corp. and is not for external distribution.
#=====================================================================
#
#
#	create_release_functions.sh - Contains all functions necessary for building a release
#
#	Takes the release version as $1 argument
#
#	The following variables must be defined in any calling script
#
#	build_dir - the directory for which to build the release
#   log_dir - the directory where logs will be kept.
#	server - the cvs server name or IP
#	remote_build_dir - the build directory on the cvs server
#	product_version - the cvs tag representing the release version
#	release_pkg - the tiger release package
#        export_flags - any additional flags to pass to cvs export
#
#
ant_cmd="c:/eclipse3.7.2/ant/apache-ant-1.6.1/bin/ant"
ant_opts="-Declipse-home=c:/eclipse3.7.2"
antlr_tool="pt_antlr"
cvsroot="/arch1/products/tiger/repository"
grm_ran="unset"
internal_modules="com.mentor.nucleus.bp.als
				  com.mentor.nucleus.bp.internal.tools
                  com.mentor.nucleus.bp.test
                  com.mentor.nucleus.bp.ui.tree"
unit_test_modules="com.mentor.nucleus.bp.als.oal.test
				   com.mentor.nucleus.bp.core.test
				   com.mentor.nucleus.bp.io.sql.test
				   com.mentor.nucleus.bp.io.mdl.test
				   com.mentor.nucleus.bp.ui.canvas.test
				   com.mentor.nucleus.bp.ui.explorer.test
				   com.mentor.nucleus.bp.ui.text.test
				   com.mentor.nucleus.bp.ui.properties.test
				   com.mentor.nucleus.bp.compare.test
				   com.mentor.nucleus.internal.test
				   com.mentor.nucleus.bp.search.test
				   MC-Java.test
				   com.mentor.nucleus.bp.welcome.test
                   com.mentor.nucleus.bp.debug.ui.test"
independent_modules="com.mentor.nucleus.bp.mc.xmiexport
					 com.mentor.nucleus.bp.mc.mc3020
					 com.mentor.nucleus.bp.mc
					 com.mentor.nucleus.bp.mc.c.source
					 com.mentor.nucleus.bp.mc.c.binary
					 com.mentor.nucleus.bp.mc.systemc.source
					 com.mentor.nucleus.bp.mc.cpp.source
					 com.mentor.nucleus.bp.mc.vhdl.source
					 com.mentor.nucleus.bp.mc.template
                     com.mentor.nucleus.help.bp.mc
                     com.mentor.nucleus.bp.ui.session
					 com.mentor.nucleus.bp.debug.ui"
feature_pkg_modules="com.mentor.nucleus.bp.dap.pkg
					 com.mentor.nucleus.bp.mc.mc3020.pkg
				 	 com.mentor.nucleus.bp.pkg
                     com.mentor.nucleus.bp.verifier.pkg"
feature_modules="com.mentor.nucleus.bp.dap.pkg-feature
				 com.mentor.nucleus.bp.mc.mc3020.pkg-feature
				 com.mentor.nucleus.bp.pkg-feature
                 com.mentor.nucleus.bp.verifier.pkg-feature"
plugin_fragments="com.mentor.nucleus.bp.core.win32.x86
                  com.mentor.nucleus.bp.core.linux.x86"
all_feature_modules="$feature_pkg_modules $feature_modules"

model_compiler_modules="model_compilers
						MC-Java"

remote_build_base="/arch1/products/tiger/nightly_build"
server="tucson.wv.mentorg.com"
build_log_dir="${log_dir}/build_logs"
compile_log_dir="${log_dir}/compile_logs"
cvs_log_dir="${log_dir}/cvs_logs/${product_version}"
rsh="ssh"
external_files="/arch1/products/tiger/extra_files_for_build"

if [ ! -d ${cvs_log_dir} ]; then
	mkdir -p ${cvs_log_dir}
fi

if [ ! -d ${build_log_dir} ]; then
	mkdir -p $build_log_dir
fi

if [ ! -d ${compile_log_dir} ]; then
	mkdir -p ${compile_log_dir}
fi

function verify_checkout {

	dir_count=`ls ${module} | wc -l`

	if [ ${dir_count} -le 1 ]; then

		if [ ${SEVERITY} = "WARN" ]; then

			echo -e "Warning:  Could not check out ${module} with the following tag: ${product_version}\n" >> ${error_file}
			SEVERITY=""

		else

			echo -e "Error checking out ${module} with tag: ${product_version}"

			return 1

		fi

	fi

}

function get_required_modules {
echo "remote build dir: ${remote_build_dir}"
echo "cvsroot: ${cvsroot}"
echo "product version: ${product_version}"
echo "export flags: ${export_flags}"
    # NOTE: Comment out the follow to skip CVS checkout of the project
	${rsh} ${server} "(cd '${remote_build_dir}'; cvs -d'${cvsroot}' export ${export_flags} -r '${product_version}' '${release_pkg}')" > ${cvs_log_dir}/cvs_export_${release_pkg}.log 2>&1
	chown -R ${USERNAME} ${release_pkg}

	if [ -e ${release_pkg}/feature.xml ]; then
	plugin_modules=`grep "<plugin id=" $build_dir/$release_pkg/feature.xml | awk -F"=" '{printf("%s\n", $2)}' | sed s/\"// | sed s/\"//`
	release_version=`awk -F"\"" '{if (/[0-9]\.[0-9]\.[0-9]/) {print $2; exit;}}' ${build_dir}/${release_pkg}/feature.xml`
    plugin_modules="${plugin_modules} ${independent_modules}"
	echo "release version: ${release_version}"
	fi
}

function extract_release_files {

	modules="${internal_modules} ${plugin_modules}"

	# Rearrange modules so that core is built first

	modules=`echo ${modules} | sed s/com.mentor.nucleus.bp.core// | sed s/^/"com.mentor.nucleus.bp.core "/`

	for module in ${modules} ${all_feature_modules} ${model_compiler_modules} ${plugin_fragments}; do
		echo "Checking out ${module} for release: ${product_version}"
        # NOTE: Comment out the follow to skip CVS checkout of the project
		${rsh} ${server} "(cd '${remote_build_dir}'; cvs -d'${cvsroot}' export ${export_flags} -r '${product_version}' '${module}')" > ${cvs_log_dir}/cvs_export_${module}.log 2>&1

		chown -R ${USERNAME} ${module}

	done

}

function extract_unit_test_modules {

	for module in ${unit_test_modules}; do
		echo "Checking out ${module} for release: ${product_version}"
        # NOTE: Comment out the follow to skip CVS checkout of the project
		${rsh} ${server} "(cd '${remote_build_dir}'; cvs -d'${cvsroot}' export ${export_flags} -r '${product_version}' '${module}')" > ${cvs_log_dir}/cvs_export_${module}.log 2>&1

		chown -R ${USERNAME} ${module}

	done


}

function build_modules {

	# remove a number of plugins from the list of modules to build and compile
    modules=`echo ${modules} | sed s/com.mentor.nucleus.bp.bld.pkg// | sed s/com.mentor.nucleus.bp.doc// | sed s/com.mentor.nucleus.bp.welcome// | sed s/com.mentor.nucleus.bp.test// | sed s/com.mentor.nucleus.help.bp.mc//`

	cd ${build_dir}

	echo -e "Checking out the pt_antlr tool"

    # NOTE: Comment out the follow to skip CVS checkout of the project
  	${rsh} ${server} "(cd '${remote_build_dir}'; cvs -d'${cvsroot}' export ${export_flags} -r '${product_version}' '${antlr_tool}')" > ${cvs_log_dir}/cvs_export_${antlr_tool}.log 2>&1
	chown -R ${USERNAME} ${antlr_tool}

	for module in ${modules}; do

		if [ -e ${module}/generate.xml ]; then

			echo -e "Building version ${product_version} of ${module}"

            # NOTE: Comment out the follow to skip building the projects
	 		${ant_cmd} ${ant_opts} -f ${module}/generate.xml nb_all > ${build_log_dir}/${module}_build.log 2>&1

		elif [ -e ${module}/build.xml ] && [ ! -e ${module}/generate.xml ]; then

			echo -e "Building version ${product_version} of ${module}"

            # NOTE: Comment out the follow to skip building the projects
	  		${ant_cmd} ${ant_opts} -f ${module}/build.xml nb_all > ${build_log_dir}/${module}_build.log 2>&1
        	fi

	done

	# Check for errors and place in a temp file for later use.

	for module in ${modules}; do

		# Special case to exclude als.oal package as its built from als

		if [ ${module} != "com.mentor.nucleus.bp.als.oal" ] && [ ${module} != "com.mentor.nucleus.bp.ui.tree" ] && [ ${module} != "com.mentor.nucleus.bp.internal.tools" ]; then

			# Check for all cases of error, failed, and failure

			error_count=`grep -c -i -w "ERROR" ${build_log_dir}/${module}_build.log`
			failed_count=`grep -c -i -w "FAILED" ${build_log_dir}/${module}_build.log`
			failure_count=`grep -c -i -w "FAILURE" ${build_log_dir}/${module}_build.log`

			if [ ${error_count} -gt 0 ] || [ ${failed_count} -gt 0 ] || [ ${failure_count} -gt 0 ]; then

				build_log_path=`cygpath -m ${build_log_dir}/${module}_build.log`
				echo -e "Errors or failures found during the build of $module.  Check ${build_log_path}.\n" >> ${error_file}

			fi

		fi

	done

	modules="${modules} com.mentor.nucleus.bp.welcome"

}

function compile_modules {

	build_modules

    # Have to make sure the plugin compilation is ordered properly, specifically that
    # some plugins are compiled after bp.mc.mc3020
   	modules=`echo ${modules} | sed s/com.mentor.nucleus.bp.docgen// | sed s/com.mentor.nucleus.bp.cdt// | sed s/com.mentor.nucleus.bp.utilities// | sed s/com.mentor.nucleus.bp.welcome// | sed s/com.mentor.nucleus.bp.cli//`
   	# Now, move bp.utilities so it compiles to before bp.mc, but after bp.mc.mc3020
    modules=`echo ${modules} | sed 's/com.mentor.nucleus.bp.mc /com.mentor.nucleus.bp.utilities com.mentor.nucleus.bp.mc /'`
    modules_to_compile_later="com.mentor.nucleus.bp.docgen com.mentor.nucleus.bp.cdt com.mentor.nucleus.bp.welcome com.mentor.nucleus.bp.cli"
    
	cd ${build_dir}

	for module in ${modules}; do

		if [ -e ${module}/generate.xml ]; then

			echo -e "Compiling version ${product_version} of ${module}"

	 		${ant_cmd} ${ant_opts} -f ${module}/generate.xml compile > ${compile_log_dir}/${module}_compile.log 2>&1

		elif [ -e ${module}/build.xml  ] && [ ! -e ${module}/generate.xml ]; then

			echo -e "Compiling version ${product_version} of ${module}"

			${ant_cmd} ${ant_opts} -f ${module}/build.xml compile > ${compile_log_dir}/${module}_compile.log 2>&1

	  	fi

    done

	for module in ${modules_to_compile_later}; do

		if [ -e ${module}/generate.xml ]; then

			echo -e "Compiling version ${product_version} of ${module}"

	 		${ant_cmd} ${ant_opts} -f ${module}/generate.xml compile > ${compile_log_dir}/${module}_compile.log 2>&1

		elif [ -e ${module}/build.xml  ] && [ ! -e ${module}/generate.xml ]; then

			echo -e "Compiling version ${product_version} of ${module}"

			${ant_cmd} ${ant_opts} -f ${module}/build.xml compile > ${compile_log_dir}/${module}_compile.log 2>&1

	  	fi

    done
    
	modules="${modules} ${modules_to_compile_later}"
    
	# Check for errors and place in a temp file for later use.
	for module in ${modules}; do

		# Special case to exclude als.oal package as its compiled from als

		if [ ${module} != "com.mentor.nucleus.bp.als.oal" ] && [ ${module} != "com.mentor.nucleus.bp.ui.tree" ] && [ ${module} != "com.mentor.nucleus.bp.internal.tools" ]; then

			# Check for all cases of error, failed, and failure

			error_count=`grep -c -i -w "ERROR" ${compile_log_dir}/${module}_compile.log`
			failed_count=`grep -c -i -w "FAILED" ${compile_log_dir}/${module}_compile.log`
			failure_count=`grep -c -i -w "FAILURE" ${compile_log_dir}/${module}_compile.log`

			if [ ${error_count} -gt 0 ] || [ ${failed_count} -gt 0 ] || [ ${failure_count} -gt 0 ]; then
				compile_log_path=`cygpath -m ${compile_log_dir}/${module}_compile.log`
				echo -e "Errors or failures found during the compilation of ${module}. Check ${compile_log_path}.\n" >> ${error_file}

			fi

		fi

	done

}


